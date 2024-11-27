package com.example._20_exercise_xml_processing.data.repositories;

import com.example._20_exercise_xml_processing.data.entities.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface CarRepository extends JpaRepository<Car, Integer> {

    Set<Car> findAllByMakeOrderByModelAscTravelledDistanceDesc(String brand);
}
