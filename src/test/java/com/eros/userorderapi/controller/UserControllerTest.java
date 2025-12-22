package com.eros.userorderapi.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.eros.userorderapi.dto.request.UserCreateRequestDTO;
import com.eros.userorderapi.model.User;
import com.eros.userorderapi.security.JwtAuthenticationFilter;
import com.eros.userorderapi.security.JwtTokenProvider;
import com.eros.userorderapi.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserService userService;

	@MockBean
    private JwtTokenProvider jwtTokenProvider;

	@MockBean
	private JwtAuthenticationFilter jwtAuthenticationFilter;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void createUser_shouldReturnCreateUsr() throws Exception {
		UserCreateRequestDTO dto = new UserCreateRequestDTO();
		dto.setName("Mario");
		dto.setEmail("mario@test.com");
		dto.setPassword("password");

		User user = new User("Mario", "mario@test.com", "econdedPassword");
		user.setId(1L);

		when(userService.createUser(any(UserCreateRequestDTO.class)))
			.thenReturn(user);

		mockMvc.perform(post("/users")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(dto)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.email").value("mario@test.com"));

	}

}
