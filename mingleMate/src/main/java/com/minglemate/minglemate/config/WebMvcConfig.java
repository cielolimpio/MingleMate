package com.minglemate.minglemate.config;

import com.minglemate.minglemate.interceptor.ResponseInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Component
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {
    private final ResponseInterceptor responseInterceptor;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                .addMapping("/**")
                .allowedOrigins("*");

    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry
                .addInterceptor(responseInterceptor)
                .addPathPatterns("/**");
    }
}
