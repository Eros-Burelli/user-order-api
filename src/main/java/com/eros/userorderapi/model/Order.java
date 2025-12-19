package com.eros.userorderapi.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.eros.userorderapi.enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private LocalDateTime createdAt;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	@JsonIgnoreProperties("{orders}")
	private User user;

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	private List<OrderItem> items;

	@Column(nullable = false, precision = 10, scale = 2)
	private BigDecimal totalAmount;

	@Enumerated(EnumType.STRING)
	private OrderStatus status;

}
