package com.saracoglu.student.system.filter;

import java.io.IOException;

import com.saracoglu.student.system.security.jwt.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private JwtService jwtService;

	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String header = request.getHeader("Authorization");
		String token = null;
		String username = null;

		// Eğer header yoksa, filtreyi sonraki filtreye yönlendirelim
		if (header == null || !header.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}

		token = header.substring(7); // "Bearer " kısmını atıyoruz
		try {
			username = jwtService.getUsernameByToken(token);
			// Token geçerli ve kullanıcı kimliği doğrulanmışsa
			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				UserDetails userDetails = userDetailsService.loadUserByUsername(username);
				// Token süresi geçmemişse ve kullanıcı bilgisi mevcutsa
				if (userDetails != null && !jwtService.isTokenExpired(token)) {
					UsernamePasswordAuthenticationToken authentication =
							new UsernamePasswordAuthenticationToken(username, null, userDetails.getAuthorities());

					authentication.setDetails(userDetails);
					// SecurityContext'e kimlik doğrulaması ekliyoruz
					SecurityContextHolder.getContext().setAuthentication(authentication);
				}
			}
		} catch (ExpiredJwtException e) {
			System.out.println("Token süresi dolmuştur : " + e.getMessage());
		} catch (Exception e) {
			System.out.println("Token doğrulama hatası : " + e.getMessage());
		}

		// Filtrenin sonraki aşamasına geçiyoruz
		filterChain.doFilter(request, response);
	}
}
