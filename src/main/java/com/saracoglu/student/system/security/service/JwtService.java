package com.saracoglu.student.system.security.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtService {

	public static final String SECRET_KEY = "5N+6yAw9UJlZGIE3ivXxkQlxnb9BauSkvcdSJ447DQE=";

	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		String role = userDetails.getAuthorities().stream()
				.map(grantedAuthority -> grantedAuthority.getAuthority())
				.findFirst()
				.orElse("ROLE_STUDENT");  // Varsayılan olarak ROLE_STUDENT
		claims.put("role", role);
		return Jwts.builder()
				.setSubject(userDetails.getUsername())  // Kullanıcı adı
				.addClaims(claims)  // Claim'leri ekliyoruz
				.setIssuedAt(new Date())  // Token oluşturulma zamanı
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 2))  // 2 saatlik geçerlilik süresi
				.signWith(getKey(), SignatureAlgorithm.HS256)  // SecretKey ile imzalama
				.compact();
	}

	public Object getClaimsByKey(String token, String key) {
		Claims claims = getClaims(token);
		return claims.get(key);
	}

	public Claims getClaims(String token) {
		return  Jwts.parser()
				.setSigningKey(getKey())
				.parseClaimsJws(token)
				.getBody();
	}

	public <T> T exportToken(String token, Function<Claims, T> claimsFunction) {
		Claims claims = getClaims(token);
		return claimsFunction.apply(claims);
	}

	public String getUsernameByToken(String token) {
		return exportToken(token, Claims::getSubject);
	}

	public String extractRole(String token) {
		Claims claims = getClaims(token);
		String role = claims.get("role", String.class);

		if (role != null && role.startsWith("ROLE_")) {
			role = role.substring(5);  // 'ROLE_' kısmını çıkarıyoruz
		}
		return role;
	}

	public boolean isTokenExpired(String token) {
		Date expirationDate = exportToken(token, Claims::getExpiration);
		return expirationDate.before(new Date());
	}

	public SecretKey getKey() {
		byte[] keyBytes = Base64.getDecoder().decode(SECRET_KEY);  // BASE64 decode
		return new SecretKeySpec(keyBytes, "HmacSHA256");  // HMAC-SHA256 için SecretKey
	}
}
