package com.eros.userorderapi.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.eros.userorderapi.dto.request.UserCreateRequestDTO;
import com.eros.userorderapi.dto.request.UserUpdateRequestDTO;
import com.eros.userorderapi.dto.response.UserResponseDTO;
import com.eros.userorderapi.enums.UserRole;
import com.eros.userorderapi.exception.InvalidCredentialsException;
import com.eros.userorderapi.exception.ResourceNotFoundException;
import com.eros.userorderapi.model.User;
import com.eros.userorderapi.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	/**
	 * Creates a new user with USER role.
	 * Validates that the email is not already in use.
	 */
	@Transactional
	public UserResponseDTO createUser(UserCreateRequestDTO dto) {

		if(userRepository.existsByEmail(dto.getEmail())) {
			throw new IllegalArgumentException("Email already in use");
		}

		User user = new User();
		user.setName(dto.getName());
		user.setEmail(dto.getEmail());
		user.setPassword(passwordEncoder.encode(dto.getPassword()));
		user.setRole(UserRole.USER);

		User saved = userRepository.save(user);

		return toResponseDTO(saved);
	}


	/**
	 * Returns all users.
	 */
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	/**
	 * Returns a user by ID.
	 * Throws ResourceNotFoundException if the user is not found.
	 */
	public UserResponseDTO getUserById(Long id) {
		User user = userRepository.findById(id)
						.orElseThrow(() -> new ResourceNotFoundException("User not found"));

		return toResponseDTO(user);
	}

	/**
	 * Updates the name and password of an existing user.
	 * Throws ResourceNotFoundException if the user is not found.
	 */
	@Transactional
	public UserResponseDTO updateUser(Long id, UserUpdateRequestDTO dto) {
		User user = userRepository.findById(id)
						.orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id ));

		user.setName(dto.getName());
		user.setPassword(passwordEncoder.encode(dto.getPassword()));

		User updated = userRepository.save(user);

		return toResponseDTO(updated);
	}


	/**
	 * Deletes a user by ID.
	 */
	public void deleteUser(Long id) {
		userRepository.deleteById(id);
	}


	/**
	 * Authenticates a user using email and password.
	 * Throws InvalidCredentialsException if the password does not match.
	 */
	public User authenticate(String email, String password) {
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));

		if(!passwordEncoder.matches(password, user.getPassword())) {
			throw new InvalidCredentialsException("Invalid credentials");
		}

		return user;
	}

	/**
	 * Converts a User entity to UserResponseDTO.
	 */
	private UserResponseDTO toResponseDTO(User user) {
		return new UserResponseDTO(
				user.getId(),
				user.getName(),
				user.getEmail()
				);
	}

}
