package com.murilocarnelos.cursomc.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Component
public class JWTUtil {
	//pega o valor que esta no application.properties
	@Value("${jwt.secret}")
	private String secret;
	
	@Value("${jwt.expiration}")
	private Long expiration;
	
	//gerando o token
    public String generateToken(String username) {
    	return Jwts.builder()
    			.setSubject(username)
    			.setExpiration(new Date(System.currentTimeMillis() + expiration))
    			.signWith(io.jsonwebtoken.SignatureAlgorithm.HS512, secret.getBytes())
    			.compact();
    }
    
    //funcao que testa se um token é valido
    public boolean tokenValido(String token) {
    	Claims claims = getClaims(token);
    	if(claims != null) {
    		String username = claims.getSubject();
    		Date expirationDate = claims.getExpiration();
    		Date now = new Date(System.currentTimeMillis());
    		if(username != null && expirationDate != null && now.before(expirationDate)) {
    			return true;
    		}
    	}
    	return false;
    }
    
    public String getUsername(String token) {
    	Claims claims = getClaims(token);
    	if(claims != null) {
    		return claims.getSubject();
    	}
    	return null;
    }	
    		
    
    private Claims getClaims(String token) {
    	try {
    		return Jwts.parser().setSigningKey(secret.getBytes()).parseClaimsJws(token).getBody();
    	}
    	catch(Exception e) {
    		return null;
    	}
    }
    
    
    
    
    
    
}
