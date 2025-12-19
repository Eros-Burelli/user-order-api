package com.eros.userorderapi.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eros.userorderapi.dto.request.OrderCreateRequestDTO;
import com.eros.userorderapi.dto.request.OrderStatusUpdateRequestDTO;
import com.eros.userorderapi.model.Order;
import com.eros.userorderapi.service.OrderService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {

	private OrderService orderService;

	@PostMapping("/user/{userId}")
	@PreAuthorize("hasRole('USER')")
	public Order createOrder(@PathVariable Long userId, @RequestBody OrderCreateRequestDTO dto) {
		return orderService.createOrder(userId, dto);
	}

	@GetMapping("/user/{userId}")
	@PreAuthorize("hasRole('USER')")
	public List<Order> getOrdersByUser(@PathVariable Long userId) {
		return orderService.getOrdersByUser(userId);
	}


	@GetMapping
	@PreAuthorize("hasRole('ADMIN')")
	public List<Order> getAllOrders() {
		return orderService.getAllOrders();
	}


	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public Order updateOrder(@PathVariable Long id, @RequestBody OrderStatusUpdateRequestDTO dto) {
		return orderService.updateOrder(id, dto.getStatus());
	}



}
