package com.saracoglu.student.system.security.controller;

import com.saracoglu.student.system.security.model.AuthenticationRequest;
import com.saracoglu.student.system.security.model.RefreshTokenRequest;
import com.saracoglu.student.system.security.service.AuthService;
import com.saracoglu.student.system.security.service.RefreshTokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class RestAuthController {

	@Autowired
	private AuthService authService;

	@Autowired
	private RefreshTokenService refreshTokenService;

	@PostMapping("/register")
	public ResponseEntity<?> register(@Valid @RequestBody AuthenticationRequest request) {
		return authService.registerUser(request);
	}

	@PostMapping("/authenticate")
	public ResponseEntity<?> authenticate(@Valid @RequestBody AuthenticationRequest request) {
		return ResponseEntity.ok(authService.authenticate(request));
	}

	@PostMapping("/refreshToken")
	public ResponseEntity<?> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
		return refreshTokenService.refreshToken(request);
	}
}
