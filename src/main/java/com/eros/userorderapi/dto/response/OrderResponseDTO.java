package com.eros.userorderapi.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDTO {
	private Long id;
	private String product;
	private String quantity;
	private BigDecimal price;
	private LocalDateTime orderDate;
}
