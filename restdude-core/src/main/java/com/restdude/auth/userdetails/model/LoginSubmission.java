package com.restdude.auth.userdetails.model;

import io.swagger.annotations.ApiModel;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * Simple DTO for login requests
 *
 */
@ApiModel(value = "Login Submission", description = "A DTO representing a login request")

public class LoginSubmission implements Serializable{

	private String username;

	private String email;
	
	private String password;

	private String resetPasswordToken;
	
	public LoginSubmission() {
		super();
	}

	public LoginSubmission(String username, String password) {
		this();
		this.username = username;
		this.password = password;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("username", username)
			.append("email", email)
			.append("password", password)
			.append("resetPasswordToken", resetPasswordToken)
			.toString();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmailOrUsername() {
		return email != null ? email : this.getUsername();
	}
	public String getPassword() {
		return this.password;
	}

	public String getUsername() {
		return this.username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setResetPasswordToken(String resetPasswordToken) {
		this.resetPasswordToken = resetPasswordToken;

	}	
	
	public String getResetPasswordToken() {
		return resetPasswordToken;
	}
}
