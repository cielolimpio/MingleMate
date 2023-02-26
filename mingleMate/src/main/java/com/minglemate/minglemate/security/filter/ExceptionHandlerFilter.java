package com.minglemate.minglemate.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minglemate.minglemate.exception.ErrorResult;
import com.minglemate.minglemate.exception.MingleMateException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class ExceptionHandlerFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            filterChain.doFilter(request, response);
        } catch (MingleMateException e){
            response.setStatus(e.getHttpStatusEnum().getCode());
            response.setContentType("application/json");
            ErrorResult errorResult = new ErrorResult(e.getHttpStatusEnum(), e.getMessage());

            ObjectMapper objectMapper = new ObjectMapper();

            try{
                response.getWriter().write(objectMapper.writeValueAsString(errorResult));
            } catch (IOException ex){
                ex.printStackTrace();
            }
        }
    }
}
