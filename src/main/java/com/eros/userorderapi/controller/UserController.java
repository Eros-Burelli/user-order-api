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
import com.eros.userorderapi.dto.request.UserUpdateRequestDTO;
import com.eros.userorderapi.dto.response.UserResponseDTO;
import com.eros.userorderapi.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/users")
@Tag(name = "Users", description = "User management endpoints")
@AllArgsConstructor
public class UserController {

	private final UserService userService;

	@PostMapping
	@Operation(summary = "Register a new user")
	public UserResponseDTO createUser(@Valid @RequestBody UserCreateRequestDTO dto) {
		return userService.createUser(dto);
	}

	@GetMapping("/{id}")
	@SecurityRequirement(name = "bearerAuth")
	@Operation(summary = "Get user by id")
	public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
		return ResponseEntity.ok(userService.getUserById(id));
	}

	@PutMapping("/{id}")
	@SecurityRequirement(name = "bearerAuth")
	@Operation(summary = "Update user by id")
	public UserResponseDTO updateUser(@PathVariable Long id, @Valid @RequestBody UserUpdateRequestDTO dto) {
		return userService.updateUser(id, dto);
	}

	@DeleteMapping("/{id}")
	@SecurityRequirement(name = "bearerAuth")
	@Operation(summary = "Delete user by id")
	public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
		userService.deleteUser(id);
		return ResponseEntity.noContent().build();
	}

}
