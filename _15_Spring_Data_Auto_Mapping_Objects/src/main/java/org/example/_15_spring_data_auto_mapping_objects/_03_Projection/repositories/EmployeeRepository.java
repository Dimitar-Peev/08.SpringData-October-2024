package org.example._15_spring_data_auto_mapping_objects._03_Projection.repositories;

import org.example._15_spring_data_auto_mapping_objects._03_Projection.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findByBirthdayBeforeOrderBySalaryDesc(LocalDate before);

    List<Employee> findByManagerIsNotNull();

    @Query("SELECT m FROM Employee AS e JOIN e.manager AS m")
    List<Employee> findManagers();
}