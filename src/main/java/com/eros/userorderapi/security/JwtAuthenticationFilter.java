package com.eros.userorderapi.security;

import java.io.IOException;
import java.util.Collections;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.eros.userorderapi.model.User;
import com.eros.userorderapi.repository.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private JwtTokenProvider tokenProvider;

	private UserRepository userRepository;

	@Override
	protected void doFilterInternal(HttpServletRequest request,
	                                HttpServletResponse response,
	                                FilterChain filterChain)
	        throws ServletException, IOException {

	    String header = request.getHeader(HttpHeaders.AUTHORIZATION);

	    if (header != null && header.startsWith("Bearer ")) {
	        String token = header.substring(7);

	        try {
	            Long userId = tokenProvider.getUserIdFromToken(token);
	            User user = userRepository.findById(userId).orElse(null);

	            if (user != null) {
	            	UsernamePasswordAuthenticationToken auth =
	            	        new UsernamePasswordAuthenticationToken(user, null,
	            	        		Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())));

	                auth.setDetails(
	                        new WebAuthenticationDetailsSource()
	                                .buildDetails(request));

	                SecurityContextHolder.getContext().setAuthentication(auth);
	            }
	        } catch (Exception e) {
	        	// Invalid or expired token. No auth, will continue as unauthenticated
	        }
	    }

	    filterChain.doFilter(request, response);
	}


}

