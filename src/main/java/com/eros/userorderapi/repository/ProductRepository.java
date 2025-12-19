package com.eros.userorderapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eros.userorderapi.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{

}
