package com.eros.userorderapi.config;

import java.util.List;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import com.eros.userorderapi.dto.response.OrderResponseDTO;
import com.eros.userorderapi.service.OrderService;

@TestConfiguration
public class TestOrderServiceConfiguration {

	@Bean
	OrderService orderService() {
        return new OrderService(
            null,
            null
        ) {
            @Override
            public List<OrderResponseDTO> getAllOrders() {
                return List.of();
            }
        };
    }
}
