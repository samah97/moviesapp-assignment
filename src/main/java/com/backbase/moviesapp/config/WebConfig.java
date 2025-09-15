package com.backbase.moviesapp.config;

import com.backbase.moviesapp.interceptors.APITokenInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final APITokenInterceptor apiTokenInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(apiTokenInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns("/swagger-**");
    }
}
