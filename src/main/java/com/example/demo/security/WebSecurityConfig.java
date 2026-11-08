package com.example.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.demo.security.Constans;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig {

    @Autowired
    private JWTAuthorizationFilter jwtAuthorizationFilter;

    @Autowired
    private ApiKeyAuthorizationFilter apiKeyAuthorizationFilter;

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(authz -> authz
                .requestMatchers(Constans.LOGIN_URL).permitAll()
                .requestMatchers("/public/**").permitAll() // incluye /public/mapa/** (Entrega 3)
                .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/api-docs/**", "/v3/api-docs/**").permitAll() // Swagger sin token
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .anyRequest().authenticated())
            .addFilterAfter(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterAfter(apiKeyAuthorizationFilter, JWTAuthorizationFilter.class);

        return http.build();
    }
}
