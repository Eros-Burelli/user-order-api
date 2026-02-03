package com.eros.userorderapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
import com.eros.userorderapi.dto.response.OrderItemResponseDTO;
import com.eros.userorderapi.dto.response.OrderResponseDTO;
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
	    product.setName("Product A");
	    product.setPrice(new BigDecimal("10.00"));

	    OrderItemRequestDTO itemDTO = new OrderItemRequestDTO(1L, 2);
	    OrderCreateRequestDTO dto = new OrderCreateRequestDTO(List.of(itemDTO));

	    when(productRepository.findById(1L)).thenReturn(Optional.of(product));
	    when(orderRepository.save(any(Order.class)))
	        .thenAnswer(invocation -> invocation.getArgument(0));

	    OrderResponseDTO response = orderService.createOrder(user, dto);

	    assertNotNull(response);
	    assertEquals(new BigDecimal("20.00"), response.totalAmount());
	    assertEquals(1, response.items().size());

	    OrderItemResponseDTO item = response.items().get(0);
	    assertEquals(1L, item.productId());
	    assertEquals("Product A", item.productName());
	    assertEquals(2, item.quantity());
	    assertEquals(new BigDecimal("10.00"), item.unitPrice());
	}

}
