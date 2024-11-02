package org.example._13_spring_data_advanced_querying.repositories;

import org.example._13_spring_data_advanced_querying.entities.Label;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LabelRepository extends JpaRepository<Label, Long> {

}