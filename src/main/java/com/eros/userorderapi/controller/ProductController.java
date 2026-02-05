package com.eros.userorderapi.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eros.userorderapi.dto.request.ProductCreateRequestDTO;
import com.eros.userorderapi.dto.response.ProductResponseDTO;
import com.eros.userorderapi.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/products")
@Tag(name = "Products", description = "Product catalog endpoints")
public class ProductController {

	private final ProductService productService;

	@GetMapping("/{id}")
	@Operation(summary = "Get product by id")
	public ProductResponseDTO getProductById(@PathVariable Long id) {
		return productService.getProductById(id);
	}

	@GetMapping
	@Operation(summary = "Get all products")
	public List<ProductResponseDTO> getAllProducts() {
		return productService.getAllProducts();
	}

	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	@SecurityRequirement(name = "bearerAuth")
	@Operation(summary = "Create a new product (ADMIN)")
	public ProductResponseDTO createProduct(@Valid @RequestBody ProductCreateRequestDTO dto) {
		return productService.createProduct(dto);
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	@SecurityRequirement(name = "bearerAuth")
	@Operation(summary = "Update a product (ADMIN)")
	public ProductResponseDTO updateProduct(@PathVariable Long id, @Valid @RequestBody ProductCreateRequestDTO dto) {
		return productService.updateProduct(id, dto);
	}

	@DeleteMapping("{id}")
	@PreAuthorize("hasRole('ADMIN')")
	@SecurityRequirement(name = "bearerAuth")
	@Operation(summary = "Delete a product (ADMIN)")
	public void deleteProduct(@PathVariable Long id) {
		productService.deleteProduct(id);
	}

}
