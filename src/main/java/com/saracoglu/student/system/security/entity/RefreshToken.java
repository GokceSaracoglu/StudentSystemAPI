package com.saracoglu.student.system.security.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;


@Entity
@Table(name = "refresh_token")
public class RefreshToken {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "refresh_token")
	private String refreshToken;
	
	@Column(name = "expire_date")
	private Date expireDate;

	@ManyToOne
	private SecurityUser user;

	public RefreshToken(Long id, String refreshToken, Date expireDate, SecurityUser user) {
		this.id = id;
		this.refreshToken = refreshToken;
		this.expireDate = expireDate;
		this.user = user;
	}

	public RefreshToken() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public SecurityUser getUser() {
		return user;
	}

	public void setUser(SecurityUser user) {
		this.user = user;
	}

	public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}
}
