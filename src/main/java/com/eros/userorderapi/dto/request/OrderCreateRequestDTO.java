package com.eros.userorderapi.dto.request;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreateRequestDTO {

	@NotEmpty(message = "Order must contain at least one item")
	@Valid
	private List<OrderItemRequestDTO> items;

}
