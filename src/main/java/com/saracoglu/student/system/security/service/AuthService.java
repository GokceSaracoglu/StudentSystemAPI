package com.saracoglu.student.system.security.service;

import com.saracoglu.student.system.security.entity.RefreshToken;
import com.saracoglu.student.system.security.entity.SecurityUser;
import com.saracoglu.student.system.security.model.AuthenticationRequest;
import com.saracoglu.student.system.security.model.AuthenticationResponse;
import com.saracoglu.student.system.security.model.Role;
import com.saracoglu.student.system.security.repository.RefreshTokenRepository;
import com.saracoglu.student.system.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private AuthenticationProvider authenticationProvider;
	
	@Autowired
	private JwtService jwtService;

	@Autowired
	private RefreshTokenRepository refreshTokenRepository;
	
	private RefreshToken createRefreshToken(SecurityUser user) {
		RefreshToken refreshToken = new RefreshToken();
		refreshToken.setRefreshToken(UUID.randomUUID().toString());
		refreshToken.setExpireDate(new Date(System.currentTimeMillis()+ 1000*60*60*4));
		refreshToken.setUser(user);
		
		return refreshToken;
	}

	public AuthenticationResponse authenticate(AuthenticationRequest request) {
		try {
			UsernamePasswordAuthenticationToken auth =
					new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
			authenticationProvider.authenticate(auth);
			
			Optional<SecurityUser> optionalUser = userRepository.findByUsername(request.getUsername());
			if (optionalUser.isEmpty()) {
				throw new UsernameNotFoundException("Kullanıcı adı bulunamadı");
			}
			String accessToken = jwtService.generateToken((UserDetails) optionalUser.get());
			
			RefreshToken refreshToken = createRefreshToken(optionalUser.get());
			refreshTokenRepository.save(refreshToken);
			
			
			return new AuthenticationResponse(accessToken, refreshToken.getRefreshToken());
		} catch (Exception e) {
			System.out.println("Kullanıcı adı veya şifre hatalı");
		}
		return null;
	}

	public void registerUser(AuthenticationRequest authRequest) {  // Role enum'u alıyoruz
		String encodedPassword = passwordEncoder.encode(authRequest.getPassword());
		Role role = authRequest.getRole() != null ? authRequest.getRole() : Role.STUDENT;
		SecurityUser user = new SecurityUser(authRequest.getUsername(), encodedPassword, role);
		userRepository.save(user);
	}
}


