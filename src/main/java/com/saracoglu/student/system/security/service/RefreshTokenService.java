package com.saracoglu.student.system.security.service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.saracoglu.student.system.security.jwt.AuthResponse;
import com.saracoglu.student.system.security.jwt.JwtService;
import com.saracoglu.student.system.security.jwt.RefreshTokenRequest;
import com.saracoglu.student.system.security.entity.RefreshToken;
import com.saracoglu.student.system.security.entity.User;
import com.saracoglu.student.system.security.repository.RefreshTokenRepository;


@Service
public class RefreshTokenService {
	
	@Autowired
	private RefreshTokenRepository refreshTokenRepository;
	
	@Autowired
	private JwtService jwtService;

	public boolean isRefreshTokenExpired(Date expiredDate) {
		return expiredDate.before(new Date());
	}


	private RefreshToken createRefreshToken(User user) {
		RefreshToken refreshToken = new RefreshToken();
		refreshToken.setRefreshToken(UUID.randomUUID().toString());
		refreshToken.setExpireDate(new Date(System.currentTimeMillis()+ 1000*60*60*4));
		refreshToken.setUser(user);
		
		return refreshToken;
	}

	public AuthResponse refreshToken(RefreshTokenRequest request) {
		Optional<RefreshToken> optional = refreshTokenRepository.findByRefreshToken(request.getRefreshToken());
		if(optional.isEmpty()) {
			System.out.println("REFRESH TOKEN GEÇERSİZDİR : " + request.getRefreshToken());
		}
		
		RefreshToken refreshToken = optional.get();
		
		if(!isRefreshTokenExpired(refreshToken.getExpireDate())) {
			System.out.println("REFRESH TOKEN EXPİRE OLMUŞTUR BABA : " + request.getRefreshToken());
		}
		refreshTokenRepository.deleteById(refreshToken.getId());

		String accessToken = jwtService.generateToken(refreshToken.getUser());
		RefreshToken savedRefreshToken= refreshTokenRepository.save(createRefreshToken(refreshToken.getUser()));
		
		// accesss 2
		// refresh 1
		
		return new AuthResponse(accessToken, savedRefreshToken.getRefreshToken());
	}

}
