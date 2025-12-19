package com.eros.userorderapi.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.eros.userorderapi.dto.request.UserCreateRequestDTO;
import com.eros.userorderapi.enums.UserRole;
import com.eros.userorderapi.exception.InvalidCredentialsException;
import com.eros.userorderapi.exception.ResourceNotFoundException;
import com.eros.userorderapi.model.User;
import com.eros.userorderapi.repository.UserRepository;

@Service
public class UserService {

	private UserRepository userRepository;

	private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public User createUser(UserCreateRequestDTO dto) {
		User user = new User();
		user.setName(dto.getName());
		user.setEmail(dto.getEmail());
		user.setPassword(passwordEncoder.encode(dto.getPassword()));
		user.setRole(UserRole.USER);

		return userRepository.save(user);
	}

	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	public Optional<User> getUserById(Long id) {
		return userRepository.findById(id);
	}

	public User updateUser(Long id, UserCreateRequestDTO dto) {
		return userRepository.findById(id).map(user -> {
			user.setName(dto.getName());
			user.setEmail(dto.getEmail());
			user.setPassword(passwordEncoder.encode(dto.getPassword()));
			return userRepository.save(user);
		}).orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
	}

	public void deleteUser(Long id) {
		userRepository.deleteById(id);
	}

	public User authenticate(String email, String password) {
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));

		if(!passwordEncoder.matches(password, user.getPassword())) {
			throw new InvalidCredentialsException("Invalid credentials");
		}

		return user;
	}


}
