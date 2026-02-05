package com.eros.userorderapi.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eros.userorderapi.dto.request.LoginRequestDTO;
import com.eros.userorderapi.dto.response.TokenResponseDTO;
import com.eros.userorderapi.model.User;
import com.eros.userorderapi.security.JwtTokenProvider;
import com.eros.userorderapi.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@Tag(name = "Auth", description = "Authentication endpoints")
@RequiredArgsConstructor
public class AuthController {

	private final UserService userService;
	private final JwtTokenProvider jwtTokenProvider;

	@PostMapping("/login")
	@Operation(summary = "Authenticate user and return JWT")
	public TokenResponseDTO login(@Valid @RequestBody LoginRequestDTO loginRequest) {
		User user = userService.authenticate(loginRequest.getEmail(), loginRequest.getPassword());
		String token = jwtTokenProvider.generateToken(user.getId(), user.getRole());

		return new TokenResponseDTO(token);
	}

}
