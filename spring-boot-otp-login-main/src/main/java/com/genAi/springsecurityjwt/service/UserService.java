package com.genAi.springsecurityjwt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.genAi.springsecurityjwt.model.User;
import com.genAi.springsecurityjwt.repository.UserRepository;



@Service
public class UserService {
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	
	@Autowired 
	UserRepository repository;
	
	public User registerUser(User user) {
	    if (user == null) {
	        throw new IllegalArgumentException("User payload is required");
	    }
	    if (user.getEmail() == null || user.getEmail().isBlank()) {
	        throw new IllegalArgumentException("Email is required");
	    }
	    if (user.getPhoneNo() == null || user.getPhoneNo().isBlank()) {
	        throw new IllegalArgumentException("Phone number is required");
	    }
	    if (user.getPassword() == null || user.getPassword().isBlank()) {
	        throw new IllegalArgumentException("Password is required");
	    }
	    if (repository.findByEmail(user.getEmail()) != null) {
	        throw new IllegalArgumentException("Email is already registered");
	    }
	    if (repository.findByPhoneNo(user.getPhoneNo()) != null) {
	        throw new IllegalArgumentException("Phone number is already registered");
	    }
	    if (passwordEncoder != null) {
	        user.setPassword(passwordEncoder.encode(user.getPassword()));
	    }
	    user.setId(null);
	    user.setCart(null);
	    return repository.save(user);
	}
	public boolean isEmailAlreadyRegistered(String email) {
	    if (email == null || email.isBlank()) {
	            return false;
        }
        return repository.findByEmail(email)!=null;
    }
	public User findByPhoneNo(String phoneNo) {
	    if (phoneNo == null || phoneNo.isBlank()) {
	        return null;
	    }
	    return repository.findByPhoneNo(phoneNo);
	}
}
