package com.eros.userorderapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.eros.userorderapi.dto.request.ProductCreateRequestDTO;
import com.eros.userorderapi.dto.response.ProductResponseDTO;
import com.eros.userorderapi.exception.ResourceNotFoundException;
import com.eros.userorderapi.model.Product;
import com.eros.userorderapi.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

	@Mock
	private ProductRepository productRepository;

	@InjectMocks
	private ProductService productService;


	@Test
	void createProduct_shouldReturnSavedProductResponseDTO() {
		ProductCreateRequestDTO dto = new ProductCreateRequestDTO("Test Product", new BigDecimal("10.00"));
		Product saved = new Product();
		saved.setId(1L);
		saved.setName("Test Product");
		saved.setPrice(new BigDecimal("10.00"));

		when(productRepository.save(any(Product.class))).thenReturn(saved);

		ProductResponseDTO result = productService.createProduct(dto);

		assertNotNull(result);
		assertEquals(1L, result.id());
		assertEquals("Test Product", result.name());
		assertEquals(new BigDecimal("10.00"), result.price());
	}

	@Test
	void updateProduct_shouldReturnUpdatedProductResponseDTO() {
		ProductCreateRequestDTO dto = new ProductCreateRequestDTO("Updated Product", new BigDecimal("20.00"));

		Product existing = new Product();
		existing.setId(1L);
		existing.setName("Old Product");
		existing.setPrice(new BigDecimal("10.00"));

		when(productRepository.findById(1L)).thenReturn(Optional.of(existing));
		when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

		ProductResponseDTO result = productService.updateProduct(1L, dto);

		assertEquals(1L, result.id());
		assertEquals("Updated Product", result.name());
		assertEquals(new BigDecimal("20.00"), result.price());
	}

	@Test
	void updateProduct_shouldThrowException_ifNotFound() {
		ProductCreateRequestDTO dto = new ProductCreateRequestDTO("Updated Product", new BigDecimal("20.00"));

		when(productRepository.findById(1L)).thenReturn(Optional.empty());

		assertThrows(ResourceNotFoundException.class, () -> productService.updateProduct(1L, dto));

	}

	@Test
	void getProductById_shouldReturnProductResponseDTO(){
		Product product = new Product();
		product.setId(1L);
		product.setName("Test Product");
		product.setPrice(new BigDecimal("10.00"));

		when(productRepository.findById(1L)).thenReturn(Optional.of(product));

		ProductResponseDTO result = productService.getProductById(1L);

		assertEquals(1L, result.id());
		assertEquals("Test Product", result.name());
		assertEquals(new BigDecimal("10.00"), result.price());
	}

	@Test
	void getProductById_shouldThrowException_ifNotFound() {
		when(productRepository.findById(1L)).thenReturn(Optional.empty());
		assertThrows(ResourceNotFoundException.class, () -> productService.getProductById(1L));
	}

	@Test
	void getAllProducts_shouldReturnListOfProductResponseDTOs() {
		Product p1 = new Product();
		p1.setId(1L);
		p1.setName("Product 1");
		p1.setPrice(new BigDecimal("10.00"));

		Product p2 = new Product();
		p2.setId(2L);
		p2.setName("Product 2");
		p2.setPrice(new BigDecimal("20.00"));

		when(productRepository.findAll()).thenReturn(List.of(p1,p2));

		List<ProductResponseDTO> result = productService.getAllProducts();

		assertEquals(2, result.size());
		assertEquals("Product 1", result.get(0).name());
		assertEquals("Product 2", result.get(1).name());
	}

	@Test
	void deleteProduct_shouldCallRepositoryDelete() {
		productService.deleteProduct(1L);
		verify(productRepository).deleteById(1L);
	}
}
