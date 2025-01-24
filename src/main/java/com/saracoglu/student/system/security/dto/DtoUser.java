package com.saracoglu.student.system.security.dto;


public class DtoUser {

	private String username;
	
	private String password;

	public String getUsername() {
		return username;
	}

	public DtoUser() {
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

	public DtoUser(String username, String password) {
		this.username = username;
		this.password = password;
	}
}
