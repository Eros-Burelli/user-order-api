package com.eros.userorderapi.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@TestConfiguration
public class OrderSecurityTestConfig {

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.csrf(csrf -> csrf.disable())
			.authorizeHttpRequests(auth -> auth
					.requestMatchers("/orders/user/**").hasRole("USER")
					.requestMatchers("/orders/**").hasRole("ADMIN")
					.anyRequest().authenticated())
			.httpBasic(Customizer.withDefaults());

	return http.build();
	}
}
