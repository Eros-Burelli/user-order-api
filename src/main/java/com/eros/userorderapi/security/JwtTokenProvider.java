package com.eros.userorderapi.security;


import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.eros.userorderapi.enums.UserRole;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {

	@Value("${jwt.secret}")
	private String JWT_SECRET;
	@Value("${jwt.expiration}")
	private long JWT_EXPIRATION;


	public String generateToken(Long userId, UserRole role) {
		Date now = new Date();
		Date expiry = new Date(now.getTime() + JWT_EXPIRATION);

		Key key = Keys.hmacShaKeyFor(JWT_SECRET.getBytes());

		return Jwts.builder()
				.setSubject(userId.toString())
				.claim("role", role.name())
				.setIssuedAt(now)
				.setExpiration(expiry)
				.signWith(key, SignatureAlgorithm.HS256)
				.compact();
	}

    public Long getUserIdFromToken(String token) {
    	Key key = Keys.hmacShaKeyFor(JWT_SECRET.getBytes());

    	Claims claims = Jwts.parserBuilder()
    			.setSigningKey(key)
    			.build()
    			.parseClaimsJws(token)
    			.getBody();

    	return Long.parseLong(claims.getSubject());
    }
}
