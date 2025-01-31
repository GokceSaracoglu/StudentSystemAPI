package com.saracoglu.student.system.security.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

public class AuthenticationRequest {

	@NotEmpty
	private String username;
	
	@NotEmpty
	private String password;

	private Role role;

	public AuthenticationRequest() {
	}

	public AuthenticationRequest(String username, String password, Role role) {
		this.username = username;
		this.password = password;
		this.role = role;
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

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
}
