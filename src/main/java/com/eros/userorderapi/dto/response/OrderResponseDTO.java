package com.eros.userorderapi.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponseDTO(
		Long id,
	    LocalDateTime createdAt,
	    BigDecimal totalAmount,
	    String status,
	    List<OrderItemResponseDTO> items
	    ) {

}
