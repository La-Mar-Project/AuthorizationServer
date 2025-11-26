package com.jjubul.authserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("https://frontend-admin-4.vercel.app"); // 프론트 도메인 명시
        config.addAllowedOrigin("https://jjubull.vercel.app"); // 프론트 도메인 명시
        config.addAllowedOrigin("https://jjubul-auth.duckdns.org");
        config.addAllowedOrigin("http://localhost:3000"); // 로컬 개발 환경
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }


}
