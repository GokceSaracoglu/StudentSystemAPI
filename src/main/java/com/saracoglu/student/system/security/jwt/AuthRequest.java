package com.saracoglu.student.system.security.jwt;

import jakarta.validation.constraints.NotEmpty;


public class AuthRequest {

	@NotEmpty
	private String username;
	
	@NotEmpty
	private String password;

	public AuthRequest(String password, String username) {
		this.password = password;
		this.username = username;
	}

	public AuthRequest() {
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
