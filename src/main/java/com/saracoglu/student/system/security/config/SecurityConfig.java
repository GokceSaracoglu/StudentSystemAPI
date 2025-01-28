package com.saracoglu.student.system.security.config;

import com.saracoglu.student.system.filter.JwtAuthenticationFilter;
import com.saracoglu.student.system.filter.LoggingFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final LoggingFilter loggingFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, LoggingFilter loggingFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.loggingFilter = loggingFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // CSRF'yi devre dışı bırakıyoruz
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/register", "/api/authenticate").permitAll() // Bu yollar herkese açık
                        .anyRequest().authenticated() // Diğer tüm yollar kimlik doğrulama ister
                )
                .addFilterBefore(loggingFilter, UsernamePasswordAuthenticationFilter.class) // Logging filtresi
                .addFilterAfter(jwtAuthenticationFilter, LoggingFilter.class); // JWT doğrulama Logging'den sonra çalışır

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class).build();
    }
}
