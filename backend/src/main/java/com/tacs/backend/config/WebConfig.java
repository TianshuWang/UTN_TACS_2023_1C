package com.tacs.backend.config;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class WebConfig implements WebMvcConfigurer {


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RateLimiterInterceptor(new RateLimiterService()))
                .addPathPatterns("/v1/auth/**")
                .addPathPatterns("/v1/events/**")
                .addPathPatterns("/v1/monitor/**");
    }
}
