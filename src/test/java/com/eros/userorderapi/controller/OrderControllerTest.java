package com.eros.userorderapi.controller;

import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.eros.userorderapi.config.OrderControllerTestConfig;
import com.eros.userorderapi.config.OrderSecurityTestConfig;
import com.eros.userorderapi.dto.request.OrderCreateRequestDTO;
import com.eros.userorderapi.dto.request.OrderItemRequestDTO;
import com.eros.userorderapi.dto.response.OrderItemResponseDTO;
import com.eros.userorderapi.dto.response.OrderResponseDTO;
import com.eros.userorderapi.enums.UserRole;
import com.eros.userorderapi.model.User;
import com.eros.userorderapi.security.CustomUserDetails;
import com.eros.userorderapi.security.JwtAuthenticationFilter;
import com.eros.userorderapi.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(
	    controllers = OrderController.class,
	    excludeFilters = {
	        @ComponentScan.Filter(
	            type = FilterType.ASSIGNABLE_TYPE,
	            classes = JwtAuthenticationFilter.class
	        )
	    }
	)
@Import({OrderControllerTestConfig.class, OrderSecurityTestConfig.class})
@ActiveProfiles("test")
class OrderControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private OrderService orderService;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void createOrder_asUser_shouldReturnOrder() throws Exception {
	    User user = new User("Mario Rossi", "mario@example.com", "password1!");
	    user.setId(1L);
	    user.setRole(UserRole.USER);
	    CustomUserDetails userDetails = new CustomUserDetails(user);
		OrderItemRequestDTO itemDTO = new OrderItemRequestDTO(1L, 2);
		OrderCreateRequestDTO dto = new OrderCreateRequestDTO(List.of(itemDTO));

		Mockito.when(orderService.createOrder(eq(user), Mockito.any(OrderCreateRequestDTO.class)))
	    .thenAnswer(i -> {
	        List<OrderItemResponseDTO> items = List.of(
	            new OrderItemResponseDTO(1L, "Product A", 2, new BigDecimal("10.00"))
	        );
	        return new OrderResponseDTO(
	            1L,
	            LocalDateTime.now(),
	            new BigDecimal("20.00"),
	            "PENDING",
	            items
	        );
	    });

		mockMvc.perform(post("/orders")
				.with(csrf())
				.with(user(userDetails))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(1))
			.andExpect(jsonPath("$.totalAmount").value(20.00));
	}

	@Test
	@WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
	void getAllOrders_asAdmin_shouldReturnOk() throws Exception {
		mockMvc.perform(get("/orders"))
			.andExpect(status().isOk());
	}

}
