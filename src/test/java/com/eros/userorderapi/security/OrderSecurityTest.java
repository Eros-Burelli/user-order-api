package com.eros.userorderapi.security;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.eros.userorderapi.config.OrderControllerTestConfig;
import com.eros.userorderapi.config.OrderSecurityTestConfig;
import com.eros.userorderapi.controller.OrderController;
import com.eros.userorderapi.dto.request.OrderCreateRequestDTO;
import com.eros.userorderapi.dto.request.OrderItemRequestDTO;
import com.eros.userorderapi.enums.UserRole;
import com.eros.userorderapi.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(
		controllers = OrderController.class,
		excludeFilters = @ComponentScan.Filter(
				type = FilterType.ASSIGNABLE_TYPE,
				classes = JwtAuthenticationFilter.class))
@Import({OrderControllerTestConfig.class, OrderSecurityTestConfig.class})
class OrderSecurityTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void userCanCreateOrder() throws Exception {
		User user = new User("Mario Rossi", "mario@exambple.com", "password");
		user.setId(1L);
		user.setRole(UserRole.USER);
		OrderCreateRequestDTO dto = new OrderCreateRequestDTO();
		dto.setItems(List.of(new OrderItemRequestDTO(1L, 2)));

		mockMvc.perform(post("/orders")
				.with(csrf())
				.with(user(new CustomUserDetails(user)))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isOk());
	}

	@Test
	void userCannotAccessAdminEndpoint() throws Exception {
		mockMvc.perform(get("/orders")
					.with(user("user").roles("USER")))
				.andExpect(status().isForbidden());
	}

	@Test
	void adminCanAccessAllOrders() throws Exception {
		mockMvc.perform(get("/orders")
					.with(user("admin").roles("ADMIN")))
				.andExpect(status().isOk());
	}

	@Test
	void anonymousCannotAccessProtectedEndpoint() throws Exception {
		mockMvc.perform(get("/orders"))
			.andExpect(status().isUnauthorized());
	}

}
