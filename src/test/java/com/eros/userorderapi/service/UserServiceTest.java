package com.eros.userorderapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.eros.userorderapi.dto.request.UserCreateRequestDTO;
import com.eros.userorderapi.dto.response.UserResponseDTO;
import com.eros.userorderapi.enums.UserRole;
import com.eros.userorderapi.model.User;
import com.eros.userorderapi.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@InjectMocks
	private UserService userService;

	@Test
	void createUser_shouldReturnUserResponseDTO() {
	    UserCreateRequestDTO dto = new UserCreateRequestDTO();
	    dto.setName("Mario");
	    dto.setEmail("mario@test.com");
	    dto.setPassword("plain");

	    User savedUser = new User();
	    savedUser.setId(1L);
	    savedUser.setName("Mario");
	    savedUser.setEmail("mario@test.com");
	    savedUser.setPassword("encoded");
	    savedUser.setRole(UserRole.USER);

	    when(passwordEncoder.encode("plain")).thenReturn("encoded");
	    when(userRepository.existsByEmail("mario@test.com")).thenReturn(false);
	    when(userRepository.save(any(User.class))).thenReturn(savedUser);

	    UserResponseDTO result = userService.createUser(dto);

	    assertEquals(1L, result.id());
	    assertEquals("Mario", result.name());
	    assertEquals("mario@test.com", result.email());
	}


	@Test
	void createUser_shouldThrownException_whenEmailAreadyExists() {
		UserCreateRequestDTO dto = new UserCreateRequestDTO();
		dto.setName("Mario");
		dto.setEmail("mario@test.com");
		dto.setPassword("plain");

		when(userRepository.existsByEmail("mario@test.com")).thenReturn(true);

		assertThrows(IllegalArgumentException.class, () -> userService.createUser(dto));

	}



}
