package com.sba.recordingserver.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOrigins("http://localhost:3000")
//                .allowedOrigins("http://localhost:5173")
//                .allowedOrigins("http://localhost:5174")
//				.allowedOrigins("https://localhost:5174")
//				.allowedOrigins("https://sba-frontend-web.vercel.app/");
    }
}
