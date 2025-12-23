package com.eros.userorderapi.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.eros.userorderapi.dto.request.UserCreateRequestDTO;
import com.eros.userorderapi.dto.response.UserResponseDTO;
import com.eros.userorderapi.enums.UserRole;
import com.eros.userorderapi.exception.InvalidCredentialsException;
import com.eros.userorderapi.exception.ResourceNotFoundException;
import com.eros.userorderapi.model.User;
import com.eros.userorderapi.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;

	private final PasswordEncoder passwordEncoder;

	public UserResponseDTO createUser(UserCreateRequestDTO dto) {
		User user = new User();
		user.setName(dto.getName());
		user.setEmail(dto.getEmail());
		user.setPassword(passwordEncoder.encode(dto.getPassword()));
		user.setRole(UserRole.USER);

		User saved = userRepository.save(user);

		return toResponseDTO(saved);
	}

	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	public UserResponseDTO getUserById(Long id) {
		User user = userRepository.findById(id)
						.orElseThrow(() -> new ResourceNotFoundException("User not found"));

		return toResponseDTO(user);
	}

	public UserResponseDTO updateUser(Long id, UserCreateRequestDTO dto) {
		User user = userRepository.findById(id)
						.orElseThrow(() -> new ResourceNotFoundException("User not found iwth id " + id ));

		user.setName(dto.getName());
		user.setEmail(dto.getEmail());
		user.setPassword(passwordEncoder.encode(dto.getPassword()));

		User updated = userRepository.save(user);

		return toResponseDTO(updated);
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

	private UserResponseDTO toResponseDTO(User user) {
		return new UserResponseDTO(
				user.getId(),
				user.getName(),
				user.getEmail()
				);
	}


}
