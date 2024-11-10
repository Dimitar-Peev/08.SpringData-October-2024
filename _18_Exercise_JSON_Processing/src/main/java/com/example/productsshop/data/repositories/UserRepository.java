package com.example.productsshop.data.repositories;

import com.example.productsshop.data.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Set<User> findAllBySoldIsNotNull();

    Set<User> findAllBySoldIsNotNullOrderBySoldDesc();
}
