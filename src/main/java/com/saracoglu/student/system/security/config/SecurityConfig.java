package com.saracoglu.student.system.security.config;

import com.saracoglu.student.system.filter.JwtAuthenticationFilter;
import com.saracoglu.student.system.filter.LoggingFilter;
import com.saracoglu.student.system.filter.RateLimitFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final JwtAuthenticationFilter jwtAuthenticationFilter;

	public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
		this.jwtAuthenticationFilter = jwtAuthenticationFilter;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				// JWT filter'ını UsernamePasswordAuthenticationFilter'dan önce ekliyoruz
				.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
				.authorizeHttpRequests((authorizeRequests) ->
						authorizeRequests
								.requestMatchers("/public/**").permitAll()  // Public endpointlere izin veriyoruz
								.anyRequest().authenticated()  // Diğer tüm istekler için kimlik doğrulama zorunluluğu
				);
		return http.build();
	}

	// AuthenticationManager bean'ini ekleyebilirsiniz
	@Bean
	public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
		return http.getSharedObject(AuthenticationManagerBuilder.class).build();
	}
	@Bean
	public FilterRegistrationBean<JwtAuthenticationFilter> jwtAuthenticationFilterRegistration() {
		FilterRegistrationBean<JwtAuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(jwtAuthenticationFilter);
		registrationBean.addUrlPatterns("/api/**");  // Bu filtreyi sadece /api/** endpointlerinde çalıştıracağız
		registrationBean.setOrder(1);  // Filtrenin çalışacağı sırayı belirliyoruz (Daha düşük değer önce çalışır)
		return registrationBean;
	}
	@Bean
	public FilterRegistrationBean<RateLimitFilter> rateLimitFilterRegistrationBean() {
		FilterRegistrationBean<RateLimitFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new RateLimitFilter());
		registrationBean.addUrlPatterns("/api/**");  // Sadece "/api/**" URL'leri için geçerli
		registrationBean.setOrder(2);  // Rate limit filtresi 2. sırada çalışacak
		return registrationBean;
	}

	@Bean
	public FilterRegistrationBean<LoggingFilter> loggingFilterRegistrationBean() {
		FilterRegistrationBean<LoggingFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new LoggingFilter());
		registrationBean.addUrlPatterns("/api/**");  // "/api/**" URL'leri için geçerli
		registrationBean.setOrder(3);  // Logging filtreyi 3. sırada çalışacak
		return registrationBean;
	}

}

