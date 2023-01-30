package com.gongyeon.gongyeon.security;

import com.gongyeon.gongyeon.enums.HttpStatusEnum;
import com.gongyeon.gongyeon.exception.GongYeonException;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try{
            String token = resolveToken((HttpServletRequest) request);

            if(token != null && jwtTokenProvider.validateToken(token)){
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            chain.doFilter(request, response);
        } catch (ExpiredJwtException e){
            log.error("Expired JWT Token", e);
            throw new GongYeonException(HttpStatusEnum.BAD_REQUEST, HttpStatusEnum.BAD_REQUEST.getDescription());
        }
    }

    private String resolveToken(HttpServletRequest request) {
        HttpServletRequest req = request;
        String bearerToken = req.getHeader("Authorization");
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")){
            return bearerToken.substring(7);
        }
        return null;
    }
}
