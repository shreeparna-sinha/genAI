package com.genAi.springsecurityjwt.model;

import lombok.Data;

import java.io.Serializable;
@Data
public class AuthRequest implements Serializable {
    private String otp;
    private String phoneNo;
    private String password;
    private String email;
}
