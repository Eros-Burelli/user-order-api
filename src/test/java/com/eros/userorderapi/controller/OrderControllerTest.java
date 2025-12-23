package com.eros.userorderapi.controller;

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
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.eros.userorderapi.config.OrderControllerTestConfig;
import com.eros.userorderapi.dto.request.OrderCreateRequestDTO;
import com.eros.userorderapi.dto.request.OrderItemRequestDTO;
import com.eros.userorderapi.dto.response.OrderItemResponseDTO;
import com.eros.userorderapi.dto.response.OrderResponseDTO;
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
@AutoConfigureMockMvc(addFilters = false)
@Import(OrderControllerTestConfig.class)
class OrderControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private OrderService orderService;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@WithMockUser(username = "user@example.com", roles = {"USER"})
	void createOrder_asUser_shouldReturnOrder() throws Exception {
		OrderItemRequestDTO itemDTO = new OrderItemRequestDTO(1L, 2);
		OrderCreateRequestDTO dto = new OrderCreateRequestDTO(List.of(itemDTO));

		Mockito.when(orderService.createOrder(Mockito.eq(1L), Mockito.any(OrderCreateRequestDTO.class)))
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


		mockMvc.perform(post("/orders/user/1")
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
