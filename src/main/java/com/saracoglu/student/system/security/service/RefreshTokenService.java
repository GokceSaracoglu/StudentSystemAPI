package com.saracoglu.student.system.security.service;

import com.saracoglu.student.system.exception.handler.ApiError;
import com.saracoglu.student.system.exception.InvalidTokenException;
import com.saracoglu.student.system.exception.RefreshTokenExpiredException;
import com.saracoglu.student.system.security.entity.RefreshToken;
import com.saracoglu.student.system.security.entity.SecurityUser;
import com.saracoglu.student.system.security.model.AuthenticationResponse;
import com.saracoglu.student.system.security.model.RefreshTokenRequest;
import com.saracoglu.student.system.security.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RefreshTokenService {

	@Autowired
	private RefreshTokenRepository refreshTokenRepository;

	@Autowired
	private JwtService jwtService;

	public boolean isRefreshTokenExpired(Date expiredDate) {
		return expiredDate.before(new Date());
	}

	private RefreshToken createRefreshToken(SecurityUser user) {
		RefreshToken refreshToken = new RefreshToken();
		refreshToken.setRefreshToken(UUID.randomUUID().toString());
		refreshToken.setExpireDate(new Date(System.currentTimeMillis()+ 1000*60*60*4));
		refreshToken.setUser(user);
		return refreshToken;
	}

	public ResponseEntity<?> refreshToken(RefreshTokenRequest request) {
		Optional<RefreshToken> optional = refreshTokenRepository.findByRefreshToken(request.getRefreshToken());
		if(optional.isEmpty()) {
			throw new InvalidTokenException("Geçersiz Refresh Token: " + request.getRefreshToken());
		}

		RefreshToken refreshToken = optional.get();

		if(isRefreshTokenExpired(refreshToken.getExpireDate())) {
			refreshTokenRepository.deleteById(refreshToken.getId());
			throw new RefreshTokenExpiredException("Refresh Token süresi dolmuş: " + request.getRefreshToken());
		}

		refreshTokenRepository.deleteById(refreshToken.getId());

		String accessToken = jwtService.generateToken(refreshToken.getUser());
		RefreshToken savedRefreshToken = refreshTokenRepository.save(createRefreshToken(refreshToken.getUser()));

		Map<String, List<String>> responseMessage = new HashMap<>();
		responseMessage.put("message", Collections.singletonList("Token başarıyla yenilendi"));

		ApiError response = new ApiError(UUID.randomUUID().toString(), new Date(), responseMessage);
		return ResponseEntity.ok(response);
	}
}
