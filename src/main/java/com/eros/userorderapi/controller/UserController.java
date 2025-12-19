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

import com.eros.userorderapi.dto.request.UserCreateRequestDTO;
import com.eros.userorderapi.dto.response.LoginRequestDTO;
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
	public User createUser(@RequestBody UserCreateRequestDTO dto) {
		return userService.createUser(dto);
	}

	@GetMapping("/{id}")
	public ResponseEntity<User> getUserById(@PathVariable Long id) {
		return userService.getUserById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PutMapping("/{id}")
	public User updateUser(@PathVariable Long id, @RequestBody UserCreateRequestDTO dto) {
		return userService.updateUser(id, dto);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
		userService.deleteUser(id);
		return ResponseEntity.noContent().build();
	}

	@PostMapping("/login")
	public String login(@RequestBody LoginRequestDTO loginRequest) {
	    User user = userService.authenticate(loginRequest.getEmail(), loginRequest.getPassword());
	    return jwtTokenProvider.generateToken(user.getId(), user.getRole());
	}

}
