package com.travelapp.travel_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // ❌ ปิด CSRF (ใช้เฉพาะ REST API)
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll() // ✅ อนุญาตทุก request
            );

        return http.build();
    }
}
