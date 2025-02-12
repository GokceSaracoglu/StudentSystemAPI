package com.saracoglu.student.system.security.service;

import com.saracoglu.student.system.exception.EntityAlreadyExistsException;
import com.saracoglu.student.system.exception.StudentNotFoundException;
import com.saracoglu.student.system.exception.handler.ApiError;
import com.saracoglu.student.system.security.entity.RefreshToken;
import com.saracoglu.student.system.security.entity.SecurityUser;
import com.saracoglu.student.system.security.model.AuthenticationRequest;
import com.saracoglu.student.system.security.model.AuthenticationResponse;
import com.saracoglu.student.system.security.repository.RefreshTokenRepository;
import com.saracoglu.student.system.security.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

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

	public AuthenticationResponse authenticate(@Valid AuthenticationRequest request) {
		authenticationProvider.authenticate(
				new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

		SecurityUser user = userRepository.findByUsername(request.getUsername())
				.orElseThrow(() -> new StudentNotFoundException("Kullanıcı bulunamadı: " + request.getUsername()));

		String accessToken = jwtService.generateToken((UserDetails) user);

		RefreshToken refreshToken = createRefreshToken(user);
		refreshTokenRepository.save(refreshToken);

		return new AuthenticationResponse(accessToken, refreshToken.getRefreshToken());
	}

	public ResponseEntity<?> registerUser(@Valid AuthenticationRequest request) {
		Optional<SecurityUser> existingUser = userRepository.findByUsername(request.getUsername());
		if (existingUser.isPresent()) {
			throw new EntityAlreadyExistsException("Bu kullanıcı adı zaten kullanılıyor: " + request.getUsername());
		}

		SecurityUser newUser = new SecurityUser();
		newUser.setUsername(request.getUsername());
		newUser.setPassword(passwordEncoder.encode(request.getPassword()));

		userRepository.save(newUser);

		Map<String, List<String>> successMessage = new HashMap<>();
		successMessage.put("message", Collections.singletonList("Kullanıcı başarıyla kaydedildi"));

		return ResponseEntity.ok(new ApiError(UUID.randomUUID().toString(), new Date(), successMessage));
	}
}
