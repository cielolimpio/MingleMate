package com.mingleMate.mingleMate.service;

import com.mingleMate.mingleMate.domain.*;
import com.mingleMate.mingleMate.enums.HttpStatusEnum;
import com.mingleMate.mingleMate.exception.MingleMateException;
import com.mingleMate.mingleMate.models.*;
import com.mingleMate.mingleMate.models.payload.request.SearchProfilesRequest;
import com.mingleMate.mingleMate.models.payload.request.UpdateProfileRequest;
import com.mingleMate.mingleMate.models.payload.response.MyPageResponse;
import com.mingleMate.mingleMate.provider.AuthenticationProvider;
import com.mingleMate.mingleMate.repository.*;
import com.mingleMate.mingleMate.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
    private final StudyFieldRepository studyFieldRepository;
    private final MemberStudyFieldRepository memberStudyFieldRepository;

    private final MatchRepository matchRepository;

    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    private final BigDecimal STUDY_FIELD_SCORE_WEIGHT = BigDecimal.valueOf(60);
    private final BigDecimal DAYS_OF_THE_WEEK_SCORE_WEIGHT = BigDecimal.valueOf(40);

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
    public UpdateProfileRequest updateProfiles(UpdateProfileRequest updateProfile) {
        Long currentMemberId = AuthenticationProvider.getCurrentMemberId();
        Member member = memberRepository.findById(currentMemberId)
                .orElseThrow(() -> new MingleMateException(HttpStatusEnum.NOT_FOUND, "Can't find User"));

        List<MemberStudyField> memberStudyFields = updateProfile.getStudyFields().stream().map(sf -> {
            StudyField studyField = studyFieldRepository.findById(sf.getStudyFieldId())
                    .orElseThrow(() -> new MingleMateException(HttpStatusEnum.NOT_FOUND, "Study Field Not Found"));
            return MemberStudyField.createMemberStudyField(member, studyField, sf.getName());
        }).collect(Collectors.toList());

        memberStudyFieldRepository.saveAll(memberStudyFields);

        member.updateMember(
                updateProfile.getName(),
                updateProfile.getGender(),
                updateProfile.getAge(),
                updateProfile.getAddress(),
                updateProfile.getPossibleDaysOfTheWeek(),
                memberStudyFields
        );

        return updateProfile;
    }

    public MyPageResponse myPage() {
        Long currentMemberId = AuthenticationProvider.getCurrentMemberId();
        Member findMember = memberRepository.findById(currentMemberId)
                .orElseThrow(() -> new MingleMateException(HttpStatusEnum.NOT_FOUND, "Can't Find User"));

        List<StudyFieldDto> studyFieldDtos = findMember.getMemberStudyFields().stream().map(msf ->
                new StudyFieldDto(msf.getStudyField().getCategory(), msf.getName())
        ).collect(Collectors.toList());

        return new MyPageResponse(
                findMember.getName(),
                findMember.getEmail(),
                findMember.getGender(),
                findMember.getAge(),
                findMember.getAddress(),
                findMember.getPossibleDaysOfTheWeek(),
                studyFieldDtos,
                findMember.getTags());
    }

    public Page<MemberProfile> searchProfiles(SearchProfilesRequest request, Pageable pageable) {
        List<StudyFieldDto> studyFieldsForSearching = request.getMemberStudyFieldIds().stream().map(msfId -> {
            MemberStudyField memberStudyField = memberStudyFieldRepository.findById(msfId)
                    .orElseThrow(() -> new MingleMateException(HttpStatusEnum.NOT_FOUND, "Member Study Field Not Found"));
            return new StudyFieldDto(memberStudyField);
        }).collect(Collectors.toList());

        BigDecimal possibleMaxScoreInStudyField = BigDecimal.valueOf(
                (long) studyFieldsForSearching.size() * (studyFieldsForSearching.size() - 1) / 2
        );

        List<MemberProfile> sortedMemberProfiles = memberRepository.searchProfiles(request).stream()
                .map(profile -> {
                    AtomicInteger studyFieldWeight = new AtomicInteger(studyFieldsForSearching.size());
                    BigDecimal studyFieldScore = studyFieldsForSearching.stream()
                            .map(studyFieldForSearching -> {
                                boolean isStudyFieldCategorySame = profile.getMemberStudyFields().stream().anyMatch(it ->
                                        it.getStudyField().getCategory().equals(studyFieldForSearching.getCategory())
                                );
                                boolean isStudyFieldNameSame = profile.getMemberStudyFields().stream()
                                        .anyMatch(it -> it.getName().equals(studyFieldForSearching.getStudyFieldName()));
                                if (isStudyFieldCategorySame && isStudyFieldNameSame) return BigDecimal.ONE;
                                else if (isStudyFieldCategorySame) return BigDecimal.valueOf(0.4);
                                else return BigDecimal.ZERO;
                            })
                            .reduce((score, e) -> score.add(e.multiply(BigDecimal.valueOf(studyFieldWeight.getAndDecrement()))))
                            .orElse(BigDecimal.ZERO)
                            .divide(possibleMaxScoreInStudyField, 7, RoundingMode.HALF_UP);

                    int numOfMatchingDaysOfTheWeek = request.getDaysOfTheWeek()
                            .countMatchingDaysOfTheWeek(profile.getPossibleDaysOfTheWeek());
                    BigDecimal daysOfTheWeekScore;
                    switch (numOfMatchingDaysOfTheWeek) {
                        case 7:
                        case 6:
                        case 5:
                        case 4: daysOfTheWeekScore = BigDecimal.valueOf(1); break;
                        case 3: daysOfTheWeekScore = BigDecimal.valueOf(0.95); break;
                        case 2: daysOfTheWeekScore =  BigDecimal.valueOf(0.8); break;
                        case 1: daysOfTheWeekScore =  BigDecimal.valueOf(0.6); break;
                        default: throw new MingleMateException(HttpStatusEnum.INTERNAL_SERVER_ERROR, "Code Error In Days Of The Week");
                    }

                    BigDecimal finalScore = studyFieldScore.multiply(STUDY_FIELD_SCORE_WEIGHT)
                            .add(daysOfTheWeekScore.multiply(DAYS_OF_THE_WEEK_SCORE_WEIGHT));

                    return Pair.of(profile, finalScore);
                })
                .sorted(Comparator.comparing(Pair<MemberProfile, BigDecimal>::getSecond).reversed())
                .map(Pair::getFirst)
                .collect(Collectors.toList());

        int start = (int) pageable.getOffset() * pageable.getPageSize();
        int end = Math.min(start + pageable.getPageSize(), sortedMemberProfiles.size());
        List<MemberProfile> content = sortedMemberProfiles.subList(start, end);

        return PageableExecutionUtils.getPage(content, pageable, sortedMemberProfiles::size);
    }

    public List<MatchDto> matchingHistory() {
        Member currentMember = AuthenticationProvider.getCurrentMember();
        List<Match> result = new ArrayList<>();

        List<Match> matchesByRequester = matchRepository.findByRequester(currentMember);
        List<Match> matchesByResponder = matchRepository.findByResponder(currentMember);
        result.addAll(matchesByRequester);
        result.addAll(matchesByResponder);

        return result.stream()
                .map(m -> new MatchDto(
                        m.getRequester().getName(),
                        m.getResponder().getName(),
                        m.getMatchingStatus(),
                        m.getRegisteredDateTime()
                ))
                .collect(Collectors.toList());
    }
}
