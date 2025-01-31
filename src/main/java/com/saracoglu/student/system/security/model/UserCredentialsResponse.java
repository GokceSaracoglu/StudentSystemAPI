package com.saracoglu.student.system.security.model;

import lombok.*;


public class UserCredentialsResponse {

	private String username;
	private String password;

	public UserCredentialsResponse() {
	}

	public UserCredentialsResponse(String username, String password) {
		this.username = username;
		this.password = password;
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
