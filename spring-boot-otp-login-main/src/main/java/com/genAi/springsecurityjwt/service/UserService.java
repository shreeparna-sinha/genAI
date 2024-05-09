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
	    if (passwordEncoder != null) {
	        // Hash the password before saving it to the database
	        user.setPassword(passwordEncoder.encode(user.getPassword()));
	    }
	    return repository.save(user);
	}
	public boolean isEmailAlreadyRegistered(String email) {
        return repository.findByEmail(email)!=null;
    }
	public User findByPhoneNo(String phoneNo) {
        return repository.findByPhoneNo(phoneNo);
    }
}
