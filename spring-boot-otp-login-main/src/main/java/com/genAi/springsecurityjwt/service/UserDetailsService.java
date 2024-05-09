package com.genAi.springsecurityjwt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.genAi.springsecurityjwt.model.AuthRequest;
import com.genAi.springsecurityjwt.model.User;
import com.genAi.springsecurityjwt.repository.UserRepository;

import java.util.ArrayList;
import java.util.Optional;
@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    @Autowired
    private UserRepository repository;
//    @Override
//    public UserDetails loadUserByUsername(String phoneNo) throws UsernameNotFoundException {
//        User user = repository.findByUserName(phoneNo);
//        if(user==null){
//            user = new User();
//            user.setUserName(phoneNo);
//            repository.save(user);
//        }
//        return new org.springframework.security.core.userdetails.User(user.getUserName(), "",
//                new ArrayList<>());
//    }
    
    @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
      User user = repository.findByEmail(email);

      System.out.println("----------------"+ user);
      if(user==null){
          user = new User();
          user.setUserName(user.getPhoneNo());
          repository.save(user);
      }
      return new org.springframework.security.core.userdetails.User(email, user.getPassword(),
              new ArrayList<>());
  }
}