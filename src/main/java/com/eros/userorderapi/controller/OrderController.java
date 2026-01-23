package com.eros.userorderapi.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eros.userorderapi.dto.request.OrderCreateRequestDTO;
import com.eros.userorderapi.dto.request.OrderStatusUpdateRequestDTO;
import com.eros.userorderapi.dto.response.OrderResponseDTO;
import com.eros.userorderapi.security.CustomUserDetails;
import com.eros.userorderapi.service.OrderService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {

	private final OrderService orderService;

	@PreAuthorize("hasRole('USER')")
	public OrderResponseDTO createOrder(@AuthenticationPrincipal CustomUserDetails userDetails, @Valid @RequestBody OrderCreateRequestDTO dto) {
		return orderService.createOrder(userDetails.getUser(), dto);
	}

	@GetMapping("/me")
	@PreAuthorize("hasRole('USER')")
	public List<OrderResponseDTO> getOrderByCurrentUser(@AuthenticationPrincipal CustomUserDetails userDetails) {
		return orderService.getMyOrders(userDetails.getUser());
	}

	@GetMapping
	@PreAuthorize("hasRole('ADMIN')")
	public List<OrderResponseDTO> getAllOrders() {
		return orderService.getAllOrders();
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public OrderResponseDTO updateOrder(@PathVariable Long id, @Valid @RequestBody OrderStatusUpdateRequestDTO dto) {
		return orderService.updateOrder(id, dto.getStatus());
	}

}
