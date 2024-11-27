package com.example._20_exercise_xml_processing.data.repositories;

import com.example._20_exercise_xml_processing.data.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashSet;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    @Query(value = "select c from  Customer c order by c.birthDate, c.isYoungDriver DESC")
    LinkedHashSet<Customer> findAllByOrderByBirthDateAscIsYoungDriverDesc();
}
