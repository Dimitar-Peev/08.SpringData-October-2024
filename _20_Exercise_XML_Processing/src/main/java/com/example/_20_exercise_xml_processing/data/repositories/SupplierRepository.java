package com.example._20_exercise_xml_processing.data.repositories;

import com.example._20_exercise_xml_processing.data.entities.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Integer> {

}
