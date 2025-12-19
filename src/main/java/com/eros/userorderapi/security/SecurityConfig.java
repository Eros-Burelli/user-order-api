package com.eros.userorderapi.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private JwtAuthenticationFilter jwtAuthenticationFilter;

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	    http
	        .csrf(csrf -> csrf.disable())
	        .authorizeHttpRequests(auth -> auth
	            .requestMatchers("/users/login", "/users").permitAll()
	            .requestMatchers("/products/**").permitAll()
	            .requestMatchers("/admin/**").hasRole("ADMIN")
	            .anyRequest().authenticated())
	        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

	    return http.build();
	}

}
