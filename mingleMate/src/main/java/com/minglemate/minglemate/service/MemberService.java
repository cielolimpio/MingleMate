package com.minglemate.minglemate.service;

import com.minglemate.minglemate.domain.*;
import com.minglemate.minglemate.enums.HttpStatusEnum;
import com.minglemate.minglemate.exception.MingleMateException;
import com.minglemate.minglemate.models.*;
import com.minglemate.minglemate.models.payload.request.*;
import com.minglemate.minglemate.models.payload.response.MyPageResponse;
import com.minglemate.minglemate.provider.AuthenticationProvider;
import com.minglemate.minglemate.repository.*;
import com.minglemate.minglemate.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final RefreshTokenRepository refreshTokenRepository;
    private final ProfileImageRepository profileImageRepository;
    private final CategoryRepository categoryRepository;
    private final MemberCategoryRepository memberCategoryRepository;

    private final MatchRepository matchRepository;

    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public TokenInfo signUp(Member member) {
        if (memberRepository.findByEmail(member.getEmail()).isPresent()) {
            throw new MingleMateException(HttpStatusEnum.CONFLICT, "This Email Already Exists");
        }

        Member memberAfterEncodingPW = getMemberAfterEncodingPW(member);
        memberRepository.save(memberAfterEncodingPW);
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(member.getEmail(), member.getPassword());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        return jwtTokenProvider.generateToken(authentication);
    }

    private Member getMemberAfterEncodingPW(Member member) {
        return Member.createMember(
                member.getName(),
                member.getEmail(),
                passwordEncoder.encode(member.getPassword())
        );
    }

    public TokenInfo login(String email, String password){
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(email, password);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        return jwtTokenProvider.generateToken(authentication);
    }

    public TokenInfo reissue(TokenInfo token){
        Authentication authentication = jwtTokenProvider.getAuthentication(token.getAccessToken());

        // redis에서 refresh Token 가져오기
        RefreshToken refreshTokenFromRedis = refreshTokenRepository.findById(authentication.getName())
                .orElseThrow(() -> new MingleMateException(HttpStatusEnum.NOT_FOUND, "Refresh Token이 존재하지 않습니다.")); // 만료됨 or 로그아웃됨
        if(!token.getRefreshToken().equals(refreshTokenFromRedis.getRefreshToken())){
            throw new MingleMateException(HttpStatusEnum.BAD_REQUEST, "토큰의 유저 정보가 일치하지 않습니다."); // FORBIDDEN
        }

        //refresh token 만료되었는지 확인 -> UNAUTHORIZED
        TokenInfo reissuedTokenDto = jwtTokenProvider.generateToken(authentication);
        RefreshToken reissuedRefreshToken = new RefreshToken(authentication.getName(), reissuedTokenDto.getRefreshToken());
        refreshTokenRepository.save(reissuedRefreshToken);

        return reissuedTokenDto;
    }

    public String logout(){
        String email = AuthenticationProvider.getCurrentMember().getEmail();

        // redis에서 해당 유저의 refreshToken 값 찾아와 지우기
        RefreshToken refreshToken = refreshTokenRepository.findById(email)
                .orElseThrow(() -> new MingleMateException(HttpStatusEnum.BAD_REQUEST, "Refresh Token이 존재하지 않습니다."));
        if(!refreshToken.getRefreshToken().isEmpty()){
            refreshTokenRepository.delete(refreshToken);
        }

        return "로그아웃 성공";
    }

    @Transactional
    public void updateProfileImages(UpdateProfileImagesRequest request) {
        Long currentMemberId = AuthenticationProvider.getCurrentMemberId();
        Member member = memberRepository.findById(currentMemberId)
                .orElseThrow(() -> new MingleMateException(HttpStatusEnum.NOT_FOUND, "Can't find User"));

        validateProfileImageIndexes(request);

        List<ProfileImage> profileImagesToCreate = request.getProfileImagesToCreate().stream().map(pi -> ProfileImage.createProfileImage(
                member,  pi.getIndex(), pi.getFilename(), pi.getOriginalS3Path(), pi.getThumbnailS3Path()
        )).collect(Collectors.toList());

        profileImageRepository.saveAll(profileImagesToCreate);
        profileImageRepository.deleteAllById(request.getProfileImageIdsToDelete());

        request.getProfileImagesToUpdate().forEach(pi -> {
            ProfileImage profileImageToUpdate = profileImageRepository.findById(pi.getProfileImageId())
                    .orElseThrow(() -> new MingleMateException(HttpStatusEnum.NOT_FOUND, "Profile Image Not Found"));

            profileImageToUpdate.updateIndex(pi.getIndex());
        });
    }

    private void validateProfileImageIndexes(UpdateProfileImagesRequest request) {
        List<Integer> indexes = request.getProfileImagesToCreate().stream()
                .map(CreateProfileImageRequest::getIndex).collect(Collectors.toList());
        indexes.addAll(
                request.getProfileImagesToUpdate().stream()
                        .map(UpdateProfileImageRequest::getIndex).collect(Collectors.toList())
        );
        List<Integer> sortedIndexes = indexes.stream().sorted().collect(Collectors.toList());

        AtomicInteger index = new AtomicInteger();
        sortedIndexes.forEach(i -> {
            if (i != index.getAndIncrement())
                throw new MingleMateException(HttpStatusEnum.CONFLICT, "Invalid Index");
        });
    }

    @Transactional
    public void updateProfile(UpdateProfileRequest updateProfile) {
        Long currentMemberId = AuthenticationProvider.getCurrentMemberId();
        Member member = memberRepository.findById(currentMemberId)
                .orElseThrow(() -> new MingleMateException(HttpStatusEnum.NOT_FOUND, "Can't find User"));

        List<MemberCategory> memberCategories = updateProfile.getCategories().stream().map(c -> {
            Category category = categoryRepository.findById(c.getCategoryId())
                    .orElseThrow(() -> new MingleMateException(HttpStatusEnum.NOT_FOUND, "Category Not Found"));
            return MemberCategory.createMemberCategory(member, category, c.getDescription(), c.getPeriodOption());
        }).collect(Collectors.toList());

        memberCategoryRepository.saveAll(memberCategories);

        member.updateMember(
                updateProfile.getName(),
                updateProfile.getGender(),
                updateProfile.getAge(),
                updateProfile.getAddress(),
                memberCategories
        );
    }

    public MyPageResponse myPage() {
        Long currentMemberId = AuthenticationProvider.getCurrentMemberId();
        Member findMember = memberRepository.findById(currentMemberId)
                .orElseThrow(() -> new MingleMateException(HttpStatusEnum.NOT_FOUND, "Can't Find User"));

        List<ProfileImageDto> profileImages = findMember.getProfileImages().stream()
                .map(ProfileImageDto::new).collect(Collectors.toList());

        List<CategoryDto> categories = findMember.getMemberCategories().stream()
                .map(CategoryDto::new).collect(Collectors.toList());

        return new MyPageResponse(
                findMember.getName(),
                findMember.getEmail(),
                findMember.getGender(),
                findMember.getAge(),
                findMember.getAddress(),
                profileImages,
                categories
        );
    }

    public Page<MemberProfile> searchProfiles(SearchProfilesRequest request, Pageable pageable) {
        Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow(() ->
                new MingleMateException(HttpStatusEnum.NOT_FOUND, "Category Not Found")
        );

        SearchProfilesDto searchProfilesDto = new SearchProfilesDto(
                request.getGender(),
                request.getAgeRange(),
                request.getAddress(),
                category,
                request.getPeriodOption()
        );

        return memberRepository.searchProfiles(searchProfilesDto, pageable);
    }

    public List<MatchDto> matchHistory() {
        Member currentMember = AuthenticationProvider.getCurrentMember();
        List<Match> result = new ArrayList<>();

        List<Match> matchesByRequester = matchRepository.findByRequester(currentMember);
        List<Match> matchesByResponder = matchRepository.findByResponder(currentMember);
        result.addAll(matchesByRequester);
        result.addAll(matchesByResponder);

        return result.stream()
                .sorted(Comparator.comparing(Match::getLastModifiedDateTime).reversed())
                .map(m -> new MatchDto(
                        m.getRequester().getName(),
                        m.getResponder().getName(),
                        m.getStatus(),
                        m.getRegisteredDateTime()
                ))
                .collect(Collectors.toList());
    }
}
