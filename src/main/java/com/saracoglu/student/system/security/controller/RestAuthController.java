package com.saracoglu.student.system.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.saracoglu.student.system.security.dto.DtoUser;
import com.saracoglu.student.system.security.jwt.AuthRequest;
import com.saracoglu.student.system.security.jwt.AuthResponse;
import com.saracoglu.student.system.security.jwt.RefreshTokenRequest;
import com.saracoglu.student.system.security.service.AuthService;
import com.saracoglu.student.system.security.service.RefreshTokenService;

import jakarta.validation.Valid;

@RestController
public class RestAuthController {
	
	@Autowired
	private AuthService authService;
	
	@Autowired
	private RefreshTokenService refreshTokenService;

	@PostMapping("/register")
	public DtoUser register(@Valid @RequestBody AuthRequest request) {
		return authService.register(request);
	}

	@PostMapping("/authenticate")
	public AuthResponse authenticate(@Valid @RequestBody AuthRequest request) {
		return authService.authenticate(request);
	}

	@PostMapping("/refreshToken")
	public AuthResponse refreshToken(@RequestBody RefreshTokenRequest request) {
		return refreshTokenService.refreshToken(request);
	}

	
}
