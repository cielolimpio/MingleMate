package com.gongyeon.gongyeon.service;

import com.gongyeon.gongyeon.domain.Member;
import com.gongyeon.gongyeon.domain.RefreshToken;
import com.gongyeon.gongyeon.domain.StudyField;
import com.gongyeon.gongyeon.enums.HttpStatusEnum;
import com.gongyeon.gongyeon.exception.GongYeonException;
import com.gongyeon.gongyeon.models.MemberProfile;
import com.gongyeon.gongyeon.payload.request.SearchProfilesRequest;
import com.gongyeon.gongyeon.provider.AuthenticationProvider;
import com.gongyeon.gongyeon.repository.MemberRepository;
import com.gongyeon.gongyeon.repository.RefreshTokenRepository;
import com.gongyeon.gongyeon.security.JwtTokenProvider;
import com.gongyeon.gongyeon.models.TokenInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
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

    private final JwtTokenProvider jwtTokenProvider;

    private final BigDecimal STUDY_FIELD_SCORE_WEIGHT = BigDecimal.valueOf(60);
    private final BigDecimal DAYS_OF_THE_WEEK_SCORE_WEIGHT = BigDecimal.valueOf(40);

    public Long signUp(Member member) {
        Member newMember = memberRepository.save(member);
        System.out.println("newMember = " + newMember);
        return newMember.getId();
    }

    public TokenInfo login(String email, String password){
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        return jwtTokenProvider.generateToken(authentication);
    }

    public TokenInfo reissue(TokenInfo token){
        Authentication authentication = jwtTokenProvider.getAuthentication(token.getAccessToken());

        // redis에서 refresh Token 가져오기
        RefreshToken refreshTokenFromRedis = refreshTokenRepository.findById(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Refresh Token이 존재하지 않습니다."));
        if(!token.getRefreshToken().equals(refreshTokenFromRedis.getRefreshToken())){
            throw new GongYeonException(HttpStatusEnum.BAD_REQUEST, "토큰의 유저 정보가 일치하지 않습니다.");
        }

        TokenInfo reissuedTokenDto = jwtTokenProvider.generateToken(authentication);
        RefreshToken reissuedRefreshToken = new RefreshToken(authentication.getName(), reissuedTokenDto.getRefreshToken());
        refreshTokenRepository.save(reissuedRefreshToken);

        return reissuedTokenDto;
    }

    public String logout(String accessToken){
        // Access Token 검증
        if (!jwtTokenProvider.validateToken(accessToken)) {
            throw new GongYeonException(HttpStatusEnum.BAD_REQUEST, "토큰의 유저 정보가 일치하지 않습니다.");
        }

        // AccessToken을 통해 해당 유저 Authentication 가져오기
        Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);

        // redis에서 해당 유저의 refreshToken 값 찾아와 지우기
        RefreshToken refreshToken = refreshTokenRepository.findById(authentication.getName())
                .orElseThrow(() -> new GongYeonException(HttpStatusEnum.BAD_REQUEST, "Refresh Token이 존재하지 않습니다."));
        if(!refreshToken.getRefreshToken().isEmpty()){
            refreshTokenRepository.delete(refreshToken);
        }

        return "로그아웃 성공";
    }

    public Page<MemberProfile> searchProfiles(SearchProfilesRequest request, Pageable pageable) {
        Long memberId = AuthenticationProvider.getCurrentMemberId();
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GongYeonException(HttpStatusEnum.NOT_FOUND, "User Not Found"));

        List<StudyField> studyFieldsForSearching = request.getStudyFields().stream()
                .map(studyFieldName ->
                        member.getStudyFields().stream()
                                .filter(it -> it.getName().equals(studyFieldName))
                                .findFirst()
                                .orElseThrow(() -> new GongYeonException(HttpStatusEnum.NOT_FOUND, "User Doesn't Have Study Field " + studyFieldName))
                ).collect(Collectors.toList());
        BigDecimal possibleMaxScoreInStudyField = BigDecimal.valueOf(
                (long) studyFieldsForSearching.size() * (studyFieldsForSearching.size() - 1) / 2
        );

        List<MemberProfile> sortedMemberProfiles = memberRepository.searchProfiles(request).stream()
                .map(profile -> {
                    AtomicInteger studyFieldWeight = new AtomicInteger(studyFieldsForSearching.size());
                    BigDecimal studyFieldScore = studyFieldsForSearching.stream()
                            .map(studyFieldForSearching -> {
                                boolean isStudyFieldNameSame = profile.getStudyFields().stream()
                                        .anyMatch(it -> it.getName().equals(studyFieldForSearching.getName()));
                                if (isStudyFieldNameSame) return BigDecimal.ONE;

                                boolean isStudyFieldCategorySame = profile.getStudyFields().stream()
                                        .anyMatch(it -> it.getCategory().equals(studyFieldForSearching.getCategory()));
                                if (isStudyFieldCategorySame) return BigDecimal.valueOf(0.4);
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
                        default: throw new GongYeonException(HttpStatusEnum.INTERNAL_SERVER_ERROR, "Code Error In Days Of The Week");
                    }

                    BigDecimal finalScore = studyFieldScore.multiply(STUDY_FIELD_SCORE_WEIGHT)
                            .add(daysOfTheWeekScore.multiply(DAYS_OF_THE_WEEK_SCORE_WEIGHT));

                    return Pair.of(profile, finalScore);
                })
                .sorted(Comparator.comparing(Pair<MemberProfile, BigDecimal>::getSecond).reversed())
                .map(Pair::getFirst)
                .collect(Collectors.toList());

        int start = (int) pageable.getOffset() * pageable.getPageSize();
        int end = start + pageable.getPageSize();
        List<MemberProfile> content = sortedMemberProfiles.subList(start, end);

        return PageableExecutionUtils.getPage(content, pageable, sortedMemberProfiles::size);
    }
}
