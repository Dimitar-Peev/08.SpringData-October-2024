package org.example._15_spring_data_auto_mapping_objects._03_Projection.services;

import org.example._15_spring_data_auto_mapping_objects._03_Projection.entities.dtos.EmployeeDTO;

import java.util.List;

public interface EmployeeService {
    List<EmployeeDTO> findEmployeesBornBefore(int bornBefore);

    List<EmployeeDTO> findAllWithManager();
}