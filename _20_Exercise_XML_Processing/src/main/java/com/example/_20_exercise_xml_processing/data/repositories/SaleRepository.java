package com.example._20_exercise_xml_processing.data.repositories;

import com.example._20_exercise_xml_processing.data.entities.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Integer> {
}
