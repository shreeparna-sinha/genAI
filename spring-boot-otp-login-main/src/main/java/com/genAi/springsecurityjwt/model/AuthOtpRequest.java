package com.genAi.springsecurityjwt.model;

import lombok.Data;

@Data
public class AuthOtpRequest {
	private String otp;
	private String phoneNo;
	private String newPassword;
}
