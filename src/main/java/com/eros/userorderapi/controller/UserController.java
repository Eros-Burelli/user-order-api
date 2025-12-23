package com.eros.userorderapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eros.userorderapi.dto.request.LoginRequestDTO;
import com.eros.userorderapi.dto.request.UserCreateRequestDTO;
import com.eros.userorderapi.dto.response.TokenResponseDTO;
import com.eros.userorderapi.dto.response.UserResponseDTO;
import com.eros.userorderapi.model.User;
import com.eros.userorderapi.security.JwtTokenProvider;
import com.eros.userorderapi.service.UserService;

import lombok.AllArgsConstructor;


@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private final JwtTokenProvider jwtTokenProvider;

	private  UserService userService;

	@PostMapping
	public UserResponseDTO createUser(@RequestBody UserCreateRequestDTO dto) {
		return userService.createUser(dto);
	}

	@GetMapping("/{id}")
	public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
		return ResponseEntity.ok(userService.getUserById(id));
	}

	@PutMapping("/{id}")
	public UserResponseDTO updateUser(@PathVariable Long id, @RequestBody UserCreateRequestDTO dto) {
		return userService.updateUser(id, dto);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
		userService.deleteUser(id);
		return ResponseEntity.noContent().build();
	}

	@PostMapping("/login")
	public TokenResponseDTO login(@RequestBody LoginRequestDTO loginRequest) {
	    User user = userService.authenticate(loginRequest.getEmail(), loginRequest.getPassword());
	    String token = jwtTokenProvider.generateToken(user.getId(), user.getRole());
	    return new TokenResponseDTO(token);
	}

}
