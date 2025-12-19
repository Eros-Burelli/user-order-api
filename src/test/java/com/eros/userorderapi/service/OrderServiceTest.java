package com.eros.userorderapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.eros.userorderapi.dto.request.OrderCreateRequestDTO;
import com.eros.userorderapi.dto.request.OrderItemRequestDTO;
import com.eros.userorderapi.exception.ResourceNotFoundException;
import com.eros.userorderapi.model.Order;
import com.eros.userorderapi.model.Product;
import com.eros.userorderapi.model.User;
import com.eros.userorderapi.repository.OrderRepository;
import com.eros.userorderapi.repository.ProductRepository;
import com.eros.userorderapi.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private ProductRepository productRepository;

	@Mock
	private OrderRepository orderRepository;

	@InjectMocks
	private OrderService orderService;

	@Test
	void testCreateOrder_success() {
		User user = new User("Mario Rossi", "mario@example.com", "password");
		user.setId(1L);

		Product product = new Product();
		product.setId(1L);
		product.setPrice(new BigDecimal("10.00"));

		OrderItemRequestDTO itemDTO = new OrderItemRequestDTO(1L, 2);
		OrderCreateRequestDTO dto = new OrderCreateRequestDTO(List.of(itemDTO));

		when(userRepository.findById(1L)).thenReturn(Optional.of(user));
		when(productRepository.findById(1L)).thenReturn(Optional.of(product));
		when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArguments()[0]);

		Order order = orderService.createOrder(1L, dto);

		assertNotNull(order);
		assertEquals(user, order.getUser());
		assertEquals(1, order.getItems().size());
		assertEquals(new BigDecimal("20.00"), order.getTotalAmount());

	}

	@Test
	void testCreateOrder_userNotFound() {
		when(userRepository.findById(1L)).thenReturn(Optional.empty());

		OrderCreateRequestDTO dto = new OrderCreateRequestDTO(List.of());

		assertThrows(ResourceNotFoundException.class, () -> {
			orderService.createOrder(1L, dto);
		});
	}

}
