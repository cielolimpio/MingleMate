package com.gongyeon.gongyeon.service;

import com.gongyeon.gongyeon.domain.Member;
import com.gongyeon.gongyeon.domain.RefreshToken;
import com.gongyeon.gongyeon.enums.HttpStatusEnum;
import com.gongyeon.gongyeon.exception.GongYeonException;
import com.gongyeon.gongyeon.repository.MemberRepository;
import com.gongyeon.gongyeon.repository.RefreshTokenRepository;
import com.gongyeon.gongyeon.security.JwtTokenProvider;
import com.gongyeon.gongyeon.models.TokenInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final RefreshTokenRepository refreshTokenRepository;

    private final JwtTokenProvider jwtTokenProvider;

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
}
