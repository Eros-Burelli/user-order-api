package com.eros.userorderapi.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.eros.userorderapi.dto.request.ProductCreateRequestDTO;
import com.eros.userorderapi.exception.ResourceNotFoundException;
import com.eros.userorderapi.model.Product;
import com.eros.userorderapi.repository.ProductRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class ProductService {

	private ProductRepository productRepository;

	public Product createProduct(ProductCreateRequestDTO dto) {
		Product product = new Product();
		product.setName(dto.getName());
		product.setPrice(dto.getPrice());
		return productRepository.save(product);
	}

	public Product updateProduct(Long id, ProductCreateRequestDTO dto) {
		return productRepository.findById(id).map(product -> {
			product.setName(dto.getName());
			product.setPrice(dto.getPrice());
			return productRepository.save(product);
		}).orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + id));
	}

	public Product getProductById(Long id) {
		return productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + id));
	}

	public List<Product> getAllProducts() {
		return productRepository.findAll();
	}

	public void deleteProduct(Long id) {
		productRepository.deleteById(id);
	}

}
