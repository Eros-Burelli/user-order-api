package com.eros.userorderapi.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eros.userorderapi.dto.request.OrderCreateRequestDTO;
import com.eros.userorderapi.dto.request.OrderStatusUpdateRequestDTO;
import com.eros.userorderapi.dto.response.OrderResponseDTO;
import com.eros.userorderapi.security.CustomUserDetails;
import com.eros.userorderapi.service.OrderService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/orders")
@Tag(name = "Orders", description = "Order management endpoints")
@SecurityRequirement(name = "bearerAuth")
public class OrderController {

	private final OrderService orderService;

	@PostMapping
	@PreAuthorize("hasRole('USER')")
	@Operation(summary = "Create a new order (USER)")
	public OrderResponseDTO createOrder(@AuthenticationPrincipal CustomUserDetails userDetails, @Valid @RequestBody OrderCreateRequestDTO dto) {
		return orderService.createOrder(userDetails.getUser(), dto);
	}

	@GetMapping("/me")
	@PreAuthorize("hasRole('USER')")
	@Operation(summary = "Get current user's order (USER)")
	public List<OrderResponseDTO> getOrderByCurrentUser(@AuthenticationPrincipal CustomUserDetails userDetails) {
		return orderService.getMyOrders(userDetails.getUser());
	}

	@GetMapping
	@PreAuthorize("hasRole('ADMIN')")
	@Operation(summary = "List all orders (ADMIN)")
	public List<OrderResponseDTO> getAllOrders() {
		return orderService.getAllOrders();
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	@Operation(summary = "Update order statu (ADMIN)")
	public OrderResponseDTO updateOrder(@PathVariable Long id, @Valid @RequestBody OrderStatusUpdateRequestDTO dto) {
		return orderService.updateOrder(id, dto.getStatus());
	}

}
