package com.example.productsshop.data.repositories;

import com.example.productsshop.data.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Set;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {


    Set<Product> findAllByPriceBetweenAndBuyerIsNull(BigDecimal lower, BigDecimal upper);
}
