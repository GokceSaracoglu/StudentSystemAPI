package com.saracoglu.student.system.security.config;

import com.saracoglu.student.system.filter.JwtAuthenticationFilter;
import com.saracoglu.student.system.security.repository.UserRepository;
import com.saracoglu.student.system.security.service.JwtService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    public SecurityConfig(JwtService jwtService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable) // CSRF korumasını devre dışı bırakıyoruz (API için yaygın)
                .authorizeHttpRequests(authz -> {
                    System.out.println("🛠️ Security Yetkilendirme Başladı");
                    authz
                            .requestMatchers("/api/authenticate", "/api/register").permitAll()
                            .requestMatchers(HttpMethod.GET, "/api/**").hasAnyRole("STUDENT", "TEACHER")
                            .requestMatchers("/api/**").hasAuthority("ROLE_TEACHER")
                            .anyRequest().authenticated();
                })
                .addFilterBefore(new JwtAuthenticationFilter(jwtService, userDetailsService()), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

@Bean
public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
    AuthenticationManagerBuilder authenticationManagerBuilder =
            http.getSharedObject(AuthenticationManagerBuilder.class);

    authenticationManagerBuilder
            .userDetailsService(userDetailsService())  // Kullanıcı detay servisini buraya tanımlarız
            .passwordEncoder(passwordEncoder());  // Şifreleme metodunu kullanırız

    return authenticationManagerBuilder.build();
}
// Kullanıcı detay servisi (veritabanından kullanıcıyı alıyoruz)
@Bean
public UserDetailsService userDetailsService() {
    return username -> userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
}

@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}
}
