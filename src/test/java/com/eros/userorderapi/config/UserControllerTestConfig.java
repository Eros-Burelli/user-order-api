package com.eros.userorderapi.config;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.eros.userorderapi.security.JwtTokenProvider;
import com.eros.userorderapi.service.UserService;

@Configuration
public class UserControllerTestConfig {

	@Bean
	UserService userService() {
		return Mockito.mock(UserService.class);
	}

	@Bean
	JwtTokenProvider jwtTokenProvider() {
		return Mockito.mock(JwtTokenProvider.class);
	}
}
