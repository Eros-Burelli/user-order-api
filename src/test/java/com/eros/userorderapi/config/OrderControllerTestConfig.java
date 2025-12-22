package com.eros.userorderapi.config;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.eros.userorderapi.service.OrderService;

@Configuration
public class OrderControllerTestConfig {

	@Bean
	OrderService orderService() {
		return Mockito.mock(OrderService.class);
	}
}
