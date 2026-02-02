package com.eros.userorderapi.service;

import java.math.BigDecimal;
import java.util.List;

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

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OrderService {

	private final OrderRepository orderRepository;
	private final ProductRepository productRepository;


	/**
	 * Creates a new order for the given user and calculates the total amount.
	 */
	@Transactional
	public OrderResponseDTO createOrder(User user, OrderCreateRequestDTO dto) {

		Order order = new Order();
		order.setUser(user);
		order.setStatus(OrderStatus.PENDING);

		List<OrderItem> items = dto.getItems().stream()
				.map(i -> {
					Product product = productRepository.findById(i.getProductId())
							.orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + i.getProductId()));
					OrderItem orderItem = new OrderItem();
					orderItem.setOrder(order);
					orderItem.setProduct(product);
					orderItem.setQuantity(i.getQuantity());
					orderItem.setUnitPrice(product.getPrice());
					return orderItem;
				}).toList();

		items.forEach(order::addItem);

		BigDecimal total = items.stream()
				.map(i -> i.getUnitPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		order.setTotalAmount(total);

		Order saved = orderRepository.save(order);

		return toResponseDTO(saved);
	}


	/**
	 * Returns all orders belonging to a specific user.
	 */
	public List<OrderResponseDTO> getMyOrders(User user) {

		return orderRepository.findByUser(user).stream()
				.map(this::toResponseDTO)
				.toList();
	}

	/*
	 * Returns all orders
	 */
	public List<OrderResponseDTO> getAllOrders(){
		return orderRepository.findAll().stream()
				.map(this::toResponseDTO)
				.toList();
	}


	/**
	 * Updates the status of an existing order.
	 */
	public OrderResponseDTO updateOrder(Long id, OrderStatus orderStatus) {
		Order order = orderRepository.findById(id)
						.orElseThrow(() -> new ResourceNotFoundException("Order not found with id " + id));
		order.setStatus(orderStatus);
		return toResponseDTO(orderRepository.save(order));
	}


	/**
	 * Converts a Order entity to OrderResponseDTO.
	 */
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
