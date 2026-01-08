package com.eros.userorderapi.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.eros.userorderapi.dto.request.OrderCreateRequestDTO;
import com.eros.userorderapi.dto.response.OrderItemResponseDTO;
import com.eros.userorderapi.dto.response.OrderResponseDTO;
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
	public OrderResponseDTO createOrder(Long userId, OrderCreateRequestDTO dto) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));

		Order order = new Order();
		order.setUser(user);
		order.setCreatedAt(LocalDateTime.now());
		order.setStatus(OrderStatus.PENDING);

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

		Order saved = orderRepository.save(order);

		return toResponseDTO(saved);
	}

	public List<OrderResponseDTO> getOrdersByUser(Long userId) {
		User user = userRepository.findById(userId)
						.orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));

		return orderRepository.findByUser(user).stream()
				.map(this::toResponseDTO)
				.toList();
	}

	public List<OrderResponseDTO> getAllOrders(){
		return orderRepository.findAll().stream()
				.map(this::toResponseDTO)
				.toList();
	}

	public OrderResponseDTO updateOrder(Long id, OrderStatus orderStatus) {
		Order order = orderRepository.findById(id)
						.orElseThrow(() -> new ResourceNotFoundException("Order not found with id " + id));
		order.setStatus(orderStatus);
		return toResponseDTO(orderRepository.save(order));
	}


	private OrderResponseDTO toResponseDTO(Order order) {

	    List<OrderItemResponseDTO> itemDTOs = order.getItems().stream()
	        .map(item -> new OrderItemResponseDTO(
	            item.getProduct().getId(),
	            item.getProduct().getName(),
	            item.getQuantity(),
	            item.getUnitPrice()
	        ))
	        .toList();

	    return new OrderResponseDTO(
	        order.getId(),
	        order.getCreatedAt(),
	        order.getTotalAmount(),
	        order.getStatus().name(),
	        itemDTOs
	    );
	}


}
