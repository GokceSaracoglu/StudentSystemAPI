package com.saracoglu.student.system.filter;

import com.saracoglu.student.system.security.service.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private JwtService jwtService;

	@Autowired
	private UserDetailsService userDetailsService;

	// Swagger yollarını tanımla
	private static final String[] SWAGGER_PATHS = {
			"/swagger-ui/**",
			"/v3/api-docs/**",
			"/swagger-ui.html"
	};

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String requestURI = request.getRequestURI();

		// Swagger yollarını atla
		for (String swaggerPath : SWAGGER_PATHS) {
			if (requestURI.startsWith(swaggerPath.replace("/**", ""))) {
				filterChain.doFilter(request, response);
				return;
			}
		}
		// Authorization Header'ı logla
		String header = request.getHeader("Authorization");
		System.out.println("📌 Authorization Header: " + header);

		String token = null;
		String username = null;

		if (header == null || !header.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}

		token = header.substring(7);
		try {
			username = jwtService.getUsernameByToken(token);

			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				UserDetails userDetails = userDetailsService.loadUserByUsername(username);

				if (userDetails != null && !jwtService.isTokenExpired(token)) {
					String role = (String) jwtService.getClaimsByKey(token, "role");
					System.out.println("📌 JWT İçinden Çıkan Rol: " + role);

					if (role != null && !role.startsWith("ROLE_")) {
						role = "ROLE_" + role; // Eğer ROLE_ prefix'i yoksa ekleyelim
					}

					List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));

					UsernamePasswordAuthenticationToken authentication =
							new UsernamePasswordAuthenticationToken(username, null, authorities);
					authentication.setDetails(userDetails);

					SecurityContextHolder.getContext().setAuthentication(authentication);
				}
			}
		} catch (ExpiredJwtException e) {
			System.out.println("Token süresi dolmuş : " + e.getMessage());
		} catch (Exception e) {
			System.out.println("Token doğrulama hatası : " + e.getMessage());
		}
		filterChain.doFilter(request, response);
	}
}