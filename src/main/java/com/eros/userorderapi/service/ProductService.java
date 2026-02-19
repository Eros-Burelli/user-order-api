package com.eros.userorderapi.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.eros.userorderapi.dto.request.ProductCreateRequestDTO;
import com.eros.userorderapi.dto.response.ProductResponseDTO;
import com.eros.userorderapi.exception.ResourceNotFoundException;
import com.eros.userorderapi.model.Product;
import com.eros.userorderapi.repository.ProductRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class ProductService {

	private final ProductRepository productRepository;

	/**
	 * Creates a new product
	 */
	@Transactional
	public ProductResponseDTO createProduct(ProductCreateRequestDTO dto) {
		log.info("Creating product name: {}", dto.getName());
		Product product = new Product();
		product.setName(dto.getName());
		product.setPrice(dto.getPrice());
		Product saved = productRepository.save(product);
		log.info("Product created id: {} - name: {}", saved.getId(), saved.getName());

		return toResponseDTO(saved);
	}

	/**
	 *	Update an existing product
	 */
	@Transactional
	public ProductResponseDTO updateProduct(Long id, ProductCreateRequestDTO dto) {
		log.info("Updating product id: {}", id);
		Product product = productRepository.findById(id)
							.orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + id));
		product.setName(dto.getName());
		product.setPrice(dto.getPrice());
		Product updated = productRepository.save(product);
		log.info("Updated product id: {} - name: {}", updated.getId(), updated.getName());

		return toResponseDTO(updated);
	}

	/**
	 * Return a product by its id
	 */
	public ProductResponseDTO getProductById(Long id) {
		log.debug("Fetching product id: {}", id);
		Product product = productRepository.findById(id)
							.orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + id));
		return toResponseDTO(product);
	}


	/**
	 * Returns all product
	 */
	public List<ProductResponseDTO> getAllProducts() {
		log.debug("Fetching all products");
		return productRepository.findAll()
				.stream()
				.map(this::toResponseDTO)
				.toList();
	}

	/*
	 * Delete a product by id
	 */
	public void deleteProduct(Long id) {
		productRepository.deleteById(id);
	}


	/**
	 * Converts a Product entity to ProductResponseDTO.
	 */
	private ProductResponseDTO toResponseDTO(Product product) {
		return new ProductResponseDTO(product.getId(), product.getName(), product.getPrice());
	}

}
