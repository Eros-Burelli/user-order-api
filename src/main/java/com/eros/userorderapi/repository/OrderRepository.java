package com.eros.userorderapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eros.userorderapi.model.Order;
import com.eros.userorderapi.model.User;

public interface OrderRepository extends JpaRepository<Order, Long> {
	List<Order> findByUser(User user);
}
