package com.eros.userorderapi.dto.request;

import com.eros.userorderapi.enums.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatusUpdateRequestDTO {
	private OrderStatus status;
}
