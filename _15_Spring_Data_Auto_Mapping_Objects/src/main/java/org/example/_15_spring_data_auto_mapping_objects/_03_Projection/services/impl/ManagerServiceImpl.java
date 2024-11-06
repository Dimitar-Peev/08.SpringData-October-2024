package org.example._15_spring_data_auto_mapping_objects._03_Projection.services.impl;

import org.example._15_spring_data_auto_mapping_objects._03_Projection.entities.Employee;
import org.example._15_spring_data_auto_mapping_objects._03_Projection.entities.dtos.EmployeeDTO;
import org.example._15_spring_data_auto_mapping_objects._03_Projection.repositories.EmployeeRepository;
import org.example._15_spring_data_auto_mapping_objects._03_Projection.services.ManagerService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManagerServiceImpl implements ManagerService {
    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;

    public ManagerServiceImpl(EmployeeRepository employeeRepository, ModelMapper modelMapper) {
        this.employeeRepository = employeeRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<EmployeeDTO> findAllManagers() {
        List<Employee> result = employeeRepository.findManagers();

        return result
                .stream()
                .map(e -> modelMapper.map(e, EmployeeDTO.class))
                .toList();
    }
}