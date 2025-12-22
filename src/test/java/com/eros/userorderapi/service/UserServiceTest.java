package com.eros.userorderapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.eros.userorderapi.dto.request.UserCreateRequestDTO;
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
	void createUser_shouldAssignDefaultRoleAndEncodePassword() {
		UserCreateRequestDTO dto = new UserCreateRequestDTO();
		dto.setName("Mario");
		dto.setEmail("mario@test.com");
		dto.setPassword("plain");

		when(passwordEncoder.encode("plain")).thenReturn("encoded");
		when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

		User user = userService.createUser(dto);

		assertEquals(UserRole.USER, user.getRole());
		assertEquals("encoded", user.getPassword());
	}

}
