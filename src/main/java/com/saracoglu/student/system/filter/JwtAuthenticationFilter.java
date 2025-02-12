package com.saracoglu.student.system.filter;

import com.saracoglu.student.system.logging.LoggingHelper;
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

	@Autowired
	private LoggingHelper loggingHelper;

	private static final String[] SWAGGER_PATHS = {
			"/swagger-ui/**",
			"/v3/api-docs/**",
			"/swagger-ui.html"
	};

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String requestId = loggingHelper.logRequest(request);
		String requestURI = request.getRequestURI();

		for (String swaggerPath : SWAGGER_PATHS) {
			if (requestURI.startsWith(swaggerPath.replace("/**", ""))) {
				filterChain.doFilter(request, response);
				return;
			}
		}

		String header = request.getHeader("Authorization");

		if (header == null || !header.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}

		String token = header.substring(7);
		try {
			String username = jwtService.getUsernameByToken(token);

			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				UserDetails userDetails = userDetailsService.loadUserByUsername(username);

				if (userDetails != null && !jwtService.isTokenExpired(token)) {
					String role = (String) jwtService.getClaimsByKey(token, "role");

					if (role != null && !role.startsWith("ROLE_")) {
						role = "ROLE_" + role;
					}

					List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));

					UsernamePasswordAuthenticationToken authentication =
							new UsernamePasswordAuthenticationToken(username, null, authorities);
					authentication.setDetails(userDetails);

					SecurityContextHolder.getContext().setAuthentication(authentication);
				}
			}
		} catch (ExpiredJwtException e) {
			loggingHelper.logError(requestId, "JWT Token süresi dolmuş", e);
		} catch (Exception e) {
			loggingHelper.logError(requestId, "JWT doğrulama hatası", e);
		}

		filterChain.doFilter(request, response);
		loggingHelper.logResponse(requestId, response);
	}
}
