package com.saracoglu.student.system.security.controller;

import com.saracoglu.student.system.security.model.AuthenticationRequest;
import com.saracoglu.student.system.security.model.AuthenticationResponse;
import com.saracoglu.student.system.security.model.RefreshTokenRequest;
import com.saracoglu.student.system.security.service.AuthService;
import com.saracoglu.student.system.security.service.RefreshTokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class RestAuthController {
	
	@Autowired
	private AuthService authService;
	
	@Autowired
	private RefreshTokenService refreshTokenService;

	@PostMapping("/register")
	public void register(@Valid @RequestBody AuthenticationRequest request) {
		 authService.registerUser(request);
	}

	@PostMapping("/authenticate")
	public AuthenticationResponse authenticate(@Valid @RequestBody AuthenticationRequest request) {
		return authService.authenticate(request);
	}

	@PostMapping("/refreshToken")
	public AuthenticationResponse refreshToken(@RequestBody RefreshTokenRequest request) {
		return refreshTokenService.refreshToken(request);
	}
}
