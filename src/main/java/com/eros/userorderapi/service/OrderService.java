package com.eros.userorderapi.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.eros.userorderapi.dto.request.OrderCreateRequestDTO;
import com.eros.userorderapi.enums.OrderStatus;
import com.eros.userorderapi.exception.ResourceNotFoundException;
import com.eros.userorderapi.model.Order;
import com.eros.userorderapi.model.OrderItem;
import com.eros.userorderapi.model.Product;
import com.eros.userorderapi.model.User;
import com.eros.userorderapi.repository.OrderRepository;
import com.eros.userorderapi.repository.ProductRepository;
import com.eros.userorderapi.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class OrderService {

	private OrderRepository orderRepository;

	private UserRepository userRepository;

	private ProductRepository productRepository;

	@Transactional
	public Order createOrder(Long userId, OrderCreateRequestDTO dto) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));

		Order order = new Order();
		order.setUser(user);
		order.setCreatedAt(LocalDateTime.now());

		List<OrderItem> items = dto.getItems().stream()
				.map(i -> {
					Product product = productRepository.findById(i.getProductId())
							.orElseThrow(() -> new RuntimeException("Product not found with id " + i.getProductId()));
					OrderItem orderItem = new OrderItem();
					orderItem.setOrder(order);
					orderItem.setProduct(product);
					orderItem.setQuantity(i.getQuantity());
					orderItem.setUnitPrice(product.getPrice());
					return orderItem;
				}).collect(Collectors.toList());

		order.setItems(items);

		BigDecimal total = items.stream()
				.map(i -> i.getUnitPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		order.setTotalAmount(total);

		return orderRepository.save(order);
	}

	public List<Order> getOrdersByUser(Long userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));

		return orderRepository.findByUser(user);
	}

	public List<Order> getAllOrders(){
		return orderRepository.findAll();
	}

	public Order updateOrder(Long id, OrderStatus orderStatus) {
		return orderRepository.findById(id).map(order -> {
			order.setStatus(orderStatus);
			return orderRepository.save(order);
		}).orElseThrow(() -> new ResourceNotFoundException("Order not found with id " + id));
	}

}
