package com.mingleMate.mingleMate.security;

import com.mingleMate.mingleMate.domain.RefreshToken;
import com.mingleMate.mingleMate.enums.HttpStatusEnum;
import com.mingleMate.mingleMate.exception.MingleMateException;
import com.mingleMate.mingleMate.models.TokenInfo;
import com.mingleMate.mingleMate.repository.MemberRepository;
import com.mingleMate.mingleMate.repository.RefreshTokenRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {

    private final Key key;
    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberRepository memberRepository;

    private final int MINUTE = 60 * 1000;
    private final int HOUR = 60 * MINUTE;
    private final int DAY = 24 * HOUR;
    private final int ACCESS_TOKEN_EXP_TIME = DAY;
    private final int REFRESH_TOKEN_EXP_TIME = 30 * DAY;
    private final SignatureAlgorithm SIGNATURE_ALG = SignatureAlgorithm.HS256;

    public JwtTokenProvider(
            @Value("${jwt.secret}") String secretKey,
            RefreshTokenRepository refreshTokenRepository,
            MemberRepository memberRepository
    ){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.refreshTokenRepository = refreshTokenRepository;
        this.memberRepository = memberRepository;
    }

    public TokenInfo generateToken(Authentication authentication) {
        // 권한 가져오기
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        // Access Token 생성
        Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXP_TIME);
        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim("auth", authorities)
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SIGNATURE_ALG)
                .compact();

        // Refresh Token 생성
        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now + REFRESH_TOKEN_EXP_TIME))
                .signWith(key, SIGNATURE_ALG)
                .compact();
        // Refresh Token 저장
        RefreshToken refToken = new RefreshToken(authentication.getName(), refreshToken);
        refreshTokenRepository.save(refToken);

        return TokenInfo.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    // JWT 토큰을 복호화하여 토큰에 들어있는 정보를 꺼내는 메서드
    public Authentication getAuthentication(String accessToken) {
        // 토큰 복호화
        Claims claims = parseClaims(accessToken);

        if (claims.get("auth") == null) {
            throw new MingleMateException(HttpStatusEnum.UNAUTHORIZED, "권한 정보가 없는 토큰입니다.");
        }

        // 클레임에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("auth").toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        // UserDetails 객체를 만들어서 Authentication 리턴
        UserDetailsImpl principal = memberRepository.findByEmail(claims.getSubject())
                .map(UserDetailsImpl::new)
                .orElseThrow(() -> new UsernameNotFoundException("Can't find User"));
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    // 토큰 정보를 검증하는 메서드
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
            throw new MingleMateException(HttpStatusEnum.UNAUTHORIZED, "Invalid JWT Token");
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
            throw new MingleMateException(HttpStatusEnum.UNAUTHORIZED, "Unsupported JWT Token");
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
            throw new MingleMateException(HttpStatusEnum.NOT_FOUND, "JWT claims string is empty.");
        } catch (ExpiredJwtException e){
            log.error("Expired JWT Token", e);
            throw new MingleMateException(HttpStatusEnum.BAD_REQUEST, HttpStatusEnum.BAD_REQUEST.getDescription());
        }
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

}
