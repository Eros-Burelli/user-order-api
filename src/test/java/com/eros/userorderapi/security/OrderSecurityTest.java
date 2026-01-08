package com.eros.userorderapi.security;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.eros.userorderapi.config.OrderControllerTestConfig;
import com.eros.userorderapi.config.TestSecurityConfig;
import com.eros.userorderapi.controller.OrderController;
import com.eros.userorderapi.dto.request.OrderCreateRequestDTO;
import com.eros.userorderapi.dto.request.OrderItemRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(
		controllers = OrderController.class,
		excludeFilters = @ComponentScan.Filter(
				type = FilterType.ASSIGNABLE_TYPE,
				classes = JwtAuthenticationFilter.class))
@Import({OrderControllerTestConfig.class, TestSecurityConfig.class})
class OrderSecurityTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@WithMockUser(roles = "USER")
	@Test
	void userCanCreateOrder() throws Exception {
		OrderCreateRequestDTO dto = new OrderCreateRequestDTO();
		dto.setItems(List.of(new OrderItemRequestDTO(1l, 2)));

		mockMvc.perform(post("/orders/user/1")
				.with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isOk());
	}

	@WithMockUser(roles = "USER")
	@Test
	void userCannotAccessAdminEndpoint() throws Exception {
		mockMvc.perform(get("/orders"))
			.andExpect(status().isForbidden());
	}

	@WithMockUser(roles = "ADMIN")
	@Test
	void adminCanAccessAllOrders() throws Exception {
		mockMvc.perform(get("/orders"))
			.andExpect(status().isOk());
	}

	@Test
	void anonymousCannotAccessProtectedEndpoint() throws Exception {
		mockMvc.perform(get("/orders"))
			.andExpect(status().isUnauthorized());
	}

}
