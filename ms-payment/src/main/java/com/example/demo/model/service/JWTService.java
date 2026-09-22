package com.example.demo.model.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JWTService {
	
	public Map<String, String> validateJWT(String token) throws Exception {
		String key = "hdgfkhdwipoqeuknvmnpewojfkdewtyhf";
		SecretKey skey = Keys.hmacShaKeyFor(key.getBytes());

		Claims claim = Jwts.parser().verifyWith(skey).build().parseSignedClaims(token).getPayload();
		if (claim == null || claim.getExpiration().before(new Date())) {
			throw new Exception("Token Invalid!");
		}
		Map<String, String> parsedJWT = new HashMap<>();
		parsedJWT.put("username", claim.get("un").toString());
		parsedJWT.put("email", claim.get("email").toString());
		parsedJWT.put("role", claim.get("role").toString());
		parsedJWT.put("id", claim.get("id").toString());
		return parsedJWT;
	}

	
	

}
