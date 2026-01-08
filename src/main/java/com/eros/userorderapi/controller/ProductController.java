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

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/products")
public class ProductController {

	private ProductService productService;

	@GetMapping("/{id}")
	public ProductResponseDTO getProductById(@PathVariable Long id) {
		return productService.getProductById(id);
	}

	@GetMapping
	public List<ProductResponseDTO> getAllProducts() {
		return productService.getAllProducts();
	}


	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ProductResponseDTO createProduct(@RequestBody ProductCreateRequestDTO dto) {
		return productService.createProduct(dto);
	}


	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ProductResponseDTO updateProduct(@PathVariable Long id, @RequestBody ProductCreateRequestDTO dto) {
		return productService.updateProduct(id, dto);
	}

	@DeleteMapping("{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public void deleteProduct(@PathVariable Long id) {
		productService.deleteProduct(id);
	}
}
