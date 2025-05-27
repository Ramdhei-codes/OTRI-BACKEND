package com.ucaldas.otri.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOriginPatterns("*") // More permissive for development
                        .allowedOrigins(
                                "http://localhost:3000",
                                "http://127.0.0.1:3000",  // Sometimes localhost resolves differently
                                "http://localhost:4200",
                                "http://localhost:8080"
                        )
                        .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS", "HEAD")
                        .allowedHeaders("*")
                        .exposedHeaders("*") // Add this if you need to expose custom headers
                        .allowCredentials(true)
                        .maxAge(3600);
            }
        };
    }
}
