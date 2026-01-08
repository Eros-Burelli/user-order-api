package com.eros.userorderapi.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.eros.userorderapi.dto.request.ProductCreateRequestDTO;
import com.eros.userorderapi.dto.response.ProductResponseDTO;
import com.eros.userorderapi.exception.ResourceNotFoundException;
import com.eros.userorderapi.model.Product;
import com.eros.userorderapi.repository.ProductRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class ProductService {

	private ProductRepository productRepository;

	public ProductResponseDTO createProduct(ProductCreateRequestDTO dto) {
		Product product = new Product();
		product.setName(dto.getName());
		product.setPrice(dto.getPrice());
		return toResponseDTO(productRepository.save(product));
	}

	public ProductResponseDTO updateProduct(Long id, ProductCreateRequestDTO dto) {
		Product product = productRepository.findById(id)
							.orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + id));
		product.setName(dto.getName());
		product.setPrice(dto.getPrice());
		return toResponseDTO(productRepository.save(product));
	}

	public ProductResponseDTO getProductById(Long id) {
		Product product = productRepository.findById(id)
							.orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + id));
		return toResponseDTO(product);
	}

	public List<ProductResponseDTO> getAllProducts() {
		return productRepository.findAll()
				.stream()
				.map(this::toResponseDTO)
				.toList();
	}

	public void deleteProduct(Long id) {
		productRepository.deleteById(id);
	}

	private ProductResponseDTO toResponseDTO(Product product) {
		return new ProductResponseDTO(product.getId(), product.getName(), product.getPrice());
	}

}
