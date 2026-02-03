package com.eros.userorderapi.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.eros.userorderapi.config.OrderSecurityTestConfig;
import com.eros.userorderapi.config.UserControllerTestConfig;
import com.eros.userorderapi.dto.request.UserCreateRequestDTO;
import com.eros.userorderapi.dto.response.UserResponseDTO;
import com.eros.userorderapi.security.JwtAuthenticationFilter;
import com.eros.userorderapi.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(
	    controllers = UserController.class,
	    excludeFilters = {
	        @ComponentScan.Filter(
	            type = FilterType.ASSIGNABLE_TYPE,
	            classes = JwtAuthenticationFilter.class
	        )
	    }
	)
@Import({UserControllerTestConfig.class, OrderSecurityTestConfig.class})
@ActiveProfiles("test")
class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserService userService;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void createUser_shouldReturnCreateUser() throws Exception {
		UserCreateRequestDTO dto = new UserCreateRequestDTO();
		dto.setName("Mario");
		dto.setEmail("mario@test.com");
		dto.setPassword("Password1@");

		UserResponseDTO userResponseDTO = new UserResponseDTO(1L, "Mario", "mario@test.com");

		when(userService.createUser(any(UserCreateRequestDTO.class)))
		    .thenReturn(userResponseDTO);


		mockMvc.perform(post("/users")
					.with(csrf())
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(dto)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.email").value("mario@test.com"));

	}

}
