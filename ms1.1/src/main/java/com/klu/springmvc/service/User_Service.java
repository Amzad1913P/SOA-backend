package com.klu.springmvc.service;

import java.util.HashMap;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.klu.springmvc.model.User;
import com.klu.springmvc.repository.User_Repository;

@Service
public class User_Service {
	@Autowired
	User_Repository repo;
	
	  @Autowired
	  Jwt_Service jwts;
	
	public Object signupservice(User u) {
		
		Map<String,Object> response=new HashMap<>();
	    try {
	    User user=repo.findByUsername(u.getUsername());
	    if(user!=null) {
	    response.put("code", 501);
	    response.put("message", "The user is already registered");
	    }
	    else {
	    u.setRole(1);
	    repo.save(u);
	    response.put("code", 200);
	    response.put("message", "User Registration is success");
	   
	    }
	    return response;
	    }catch(Exception e) {
	      response.put("code", 501);
	      response.put("message",e.getMessage());  
	      return response;
	}
}
	
	public Object signinservice(Map<String,String> u) {
		Map<String,Object> response = new HashMap<>();
		try {
			User user = repo.findByUsername(u.get("username"));
			
			if(user == null || !user.getPassword().equals(u.get("password"))) {
				response.put("code",501);
				response.put("message","Authentication failed %#%#%");
			}else {
				response.put("code",200);
				response.put("message","----User Authenticaion is Success ----");
				 response.put("JWT Token", jwts.generateJWT(u, user.getRole().toString(),user.getEmail().toString(), user.getId()));
			}
		}catch(Exception e) {
			response.put("code", 501);
		    response.put("message",e.getMessage());  
		}
		return response;
	}
	
	
}
