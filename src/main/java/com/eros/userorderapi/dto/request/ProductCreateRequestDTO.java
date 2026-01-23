package com.eros.userorderapi.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductCreateRequestDTO {

	@NotBlank(message = "Name is required")
	private String name;

	@NotNull(message = "Price is required")
	@Positive(message = "Price must be greater than 0")
	private BigDecimal price;

}
