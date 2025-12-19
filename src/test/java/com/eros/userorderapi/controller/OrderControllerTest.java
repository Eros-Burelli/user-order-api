package com.eros.userorderapi.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.eros.userorderapi.dto.request.OrderCreateRequestDTO;
import com.eros.userorderapi.dto.request.OrderItemRequestDTO;
import com.eros.userorderapi.model.Order;
import com.eros.userorderapi.security.JwtAuthenticationFilter;
import com.eros.userorderapi.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(OrderController.class)
@AutoConfigureMockMvc(addFilters = false)
class OrderControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private OrderService orderService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@WithMockUser(username = "user@example.com", roles = {"USER"})
	void createOrder_asUser_shouldReturnOrder() throws Exception {
		OrderItemRequestDTO itemDTO = new OrderItemRequestDTO(1L, 2);
		OrderCreateRequestDTO dto = new OrderCreateRequestDTO(List.of(itemDTO));

		Mockito.when(orderService.createOrder(Mockito.eq(1L), Mockito.any(OrderCreateRequestDTO.class)))
			.thenAnswer(i -> {
				var order = new Order();
				order.setId(1L);
				order.setUser(null);
				order.setTotalAmount(new BigDecimal("20.00"));
				return order;
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
