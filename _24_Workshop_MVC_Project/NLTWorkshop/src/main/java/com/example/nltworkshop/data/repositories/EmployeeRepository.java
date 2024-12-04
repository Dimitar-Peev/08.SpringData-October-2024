package com.example.nltworkshop.data.repositories;

import com.example.nltworkshop.data.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    List<Employee> findByAgeGreaterThan(int age);
}