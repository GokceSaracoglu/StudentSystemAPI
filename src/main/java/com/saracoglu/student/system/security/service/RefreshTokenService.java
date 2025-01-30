package com.saracoglu.student.system.security.service;

import com.saracoglu.student.system.security.entity.RefreshToken;
import com.saracoglu.student.system.security.entity.SecurityUser;
import com.saracoglu.student.system.security.model.AuthenticationResponse;
import com.saracoglu.student.system.security.model.RefreshTokenRequest;
import com.saracoglu.student.system.security.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;


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

	public AuthenticationResponse refreshToken(RefreshTokenRequest request) {
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
		return new AuthenticationResponse(accessToken, savedRefreshToken.getRefreshToken());
	}
}
