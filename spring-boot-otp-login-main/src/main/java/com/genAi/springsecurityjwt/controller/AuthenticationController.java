package com.genAi.springsecurityjwt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.genAi.springsecurityjwt.model.AuthOtpRequest;
import com.genAi.springsecurityjwt.model.AuthRequest;
import com.genAi.springsecurityjwt.model.User;
import com.genAi.springsecurityjwt.repository.UserRepository;
import com.genAi.springsecurityjwt.service.OtpService;
import com.genAi.springsecurityjwt.service.UserDetailsService;
import com.genAi.springsecurityjwt.service.UserService;
import com.genAi.springsecurityjwt.util.JwtUtil;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "api/client/auth/")
@CrossOrigin("*")
public class AuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtTokenUtil;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private OtpService otpService;
    @Autowired
    private UserService userService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private UserRepository userRepository;

    @RequestMapping("hello")
    public String firstPage() {
        return "Hello World";
    }

    @RequestMapping(value = "requestOtp/{phoneNo}",method = RequestMethod.GET)
  public Map<String,Object> getOtp_Real(@PathVariable String phoneNo){
    	Map<String,Object> returnMap=new HashMap<>();
    	 User user = userService.findByPhoneNo(phoneNo);
    	 System.out.println("---------------------- 5");
    	 System.out.println(user);
    if(user!=null) {
        try{
            //generate OTP
            String otp = otpService.generateOtp(phoneNo);
            returnMap.put("otp", otp);
            returnMap.put("status","success");
            returnMap.put("message","Otp sent successfully");
        }catch (Exception e){
            returnMap.put("status","failed");
            returnMap.put("message",e.getMessage());
            
        }

        return returnMap;
        
    }
	return returnMap;
      
  }
    @RequestMapping(value = "verifyOtp",method = RequestMethod.POST)
  public Map<String,Object> verifyOtp_Real(@RequestBody AuthOtpRequest auth){
      Map<String,Object> returnMap=new HashMap<>();
      try{
          //verify otp
    	  System.out.println("---------------"+auth);
          if(auth.getOtp().equals(otpService.getCacheOtp(auth.getPhoneNo()))){
              User user = userService.findByPhoneNo(auth.getPhoneNo());
              
              System.out.println("------------------------- 3");
              System.out.println(user);
              String encodedPassword = passwordEncoder.encode(auth.getNewPassword());
              user.setPassword(encodedPassword);
              System.out.println("------------------------- 4");
              userRepository.save(user);
              System.out.println(user);
              
//              user.setPassword(auth.getNewPassword());
              returnMap.put("status","success");
            returnMap.put("message","new password set");

              otpService.clearOtp(auth.getPhoneNo());
          }else{
              returnMap.put("status","success");
              returnMap.put("message","Otp is either expired or incorrect");
          }

      } catch (Exception e){
          returnMap.put("status","failed");
          returnMap.put("message",e.getMessage());
      }

      return returnMap;
  }
    
//    @RequestMapping(value = "requestOtp/{phoneNo}",method = RequestMethod.GET)
//    public Map<String,Object> getOtp(@PathVariable String phoneNo){
//        Map<String,Object> returnMap=new HashMap<>();
//        try{
//            //generate OTP
//            String otp = otpService.generateOtp(phoneNo);
//            returnMap.put("otp", otp);
//            returnMap.put("status","success");
//            returnMap.put("message","Otp sent successfully");
//        }catch (Exception e){
//            returnMap.put("status","failed");
//            returnMap.put("message",e.getMessage());
//        }
//
//        return returnMap;
//    }
    
  @RequestMapping(value = "register",method = RequestMethod.POST)
  public Map<String,Object> getOtp(@RequestBody User user){
      Map<String,Object> returnMap=new HashMap<>();
      try{
    	  if (userService.isEmailAlreadyRegistered(user.getEmail())
    			  && userService.findByPhoneNo(user.getPhoneNo())!=null
    			  ) {
    		 throw new Exception("User already exist");    
    	  }
    	  User registeredUser = userService.registerUser(user);
//          String otp = otpService.generateOtp(user.getPhoneNo());
//          returnMap.put("otp", otp);
          returnMap.put("status","success");
//          returnMap.put("message","Otp sent successfully");
          returnMap.put("user", registeredUser);
      }catch (Exception e){
          returnMap.put("status","failed");
          returnMap.put("message",e.getMessage());
      }

      return returnMap;
  }

//    @RequestMapping(value = "verifyOtp",method = RequestMethod.POST)
//    public Map<String,Object> verifyOtp(@RequestBody AuthRequest authenticationRequest){
//        Map<String,Object> returnMap=new HashMap<>();
//        try{
//            //verify otp
//            if(authenticationRequest.getOtp().equals(otpService.getCacheOtp(authenticationRequest.getPhoneNo()))){
//                String jwtToken = createAuthenticationToken(authenticationRequest);
//                returnMap.put("status","success");
//                returnMap.put("message","Otp verified successfully");
//                returnMap.put("jwt",jwtToken);
//                otpService.clearOtp(authenticationRequest.getPhoneNo());
//            }else{
//                returnMap.put("status","success");
//                returnMap.put("message","Otp is either expired or incorrect");
//            }
//
//        } catch (Exception e){
//            returnMap.put("status","failed");
//            returnMap.put("message",e.getMessage());
//        }
//
//        return returnMap;
//    }
  
  
  
  @RequestMapping(value = "login",method = RequestMethod.POST)
public Map<String,Object> verifyOtp(@RequestBody AuthRequest authenticationRequest){
    Map<String,Object> returnMap=new HashMap<>();
    try{
        //verify otp
    	
        if(userService.isEmailAlreadyRegistered(authenticationRequest.getEmail())){
        	 System.out.println("----------------------------------- 1");
            String jwtToken = createAuthenticationToken(authenticationRequest);
            returnMap.put("status","success");       
            returnMap.put("jwt",jwtToken);
        }else{
            returnMap.put("status","success");
            returnMap.put("message","Otp is either expired or incorrect");
        }

    } catch (Exception e){
        returnMap.put("status","failed");
        returnMap.put("message",e.getMessage());
    }

    return returnMap;
}

    //create auth token
//    public String createAuthenticationToken(AuthRequest authenticationRequest) throws Exception {
//        try {
//            authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(authenticationRequest.getPhoneNo(), "")
//            );
//        }
//        catch (BadCredentialsException e) {
//            throw new Exception("Incorrect username or password", e);
//        }
//        final UserDetails userDetails = userDetailsService
//                .loadUserByUsername(authenticationRequest.getPhoneNo());
//        return jwtTokenUtil.generateToken(userDetails);
//    }
  
  
  public String createAuthenticationToken(AuthRequest authenticationRequest) throws Exception {
    try {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword())
        );
    }
    catch (BadCredentialsException e) {
        throw new Exception("Incorrect username or password", e);
    }
    
    System.out.println("----------------------------------- 2");
    final UserDetails userDetails = userDetailsService
            .loadUserByUsername(authenticationRequest.getEmail());
    return jwtTokenUtil.generateToken(userDetails);
}
}
