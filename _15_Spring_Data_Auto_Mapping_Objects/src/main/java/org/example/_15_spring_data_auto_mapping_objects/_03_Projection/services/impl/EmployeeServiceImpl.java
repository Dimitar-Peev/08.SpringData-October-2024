package org.example._15_spring_data_auto_mapping_objects._03_Projection.services.impl;

import org.example._15_spring_data_auto_mapping_objects._03_Projection.entities.Employee;
import org.example._15_spring_data_auto_mapping_objects._03_Projection.entities.dtos.EmployeeDTO;
import org.example._15_spring_data_auto_mapping_objects._03_Projection.repositories.EmployeeRepository;
import org.example._15_spring_data_auto_mapping_objects._03_Projection.services.EmployeeService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, ModelMapper modelMapper) {
        this.employeeRepository = employeeRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<EmployeeDTO> findEmployeesBornBefore(int bornBefore) {
        LocalDate before = LocalDate.of(bornBefore, 1, 1);

        List<Employee> employees = employeeRepository
                .findByBirthdayBeforeOrderBySalaryDesc(before);

        return employees
                .stream()
                .map(e -> modelMapper.map(e, EmployeeDTO.class))
                .toList();
    }

    @Override
    public List<EmployeeDTO> findAllWithManager() {
        List<Employee> result = employeeRepository.findByManagerIsNotNull();

        return result
                .stream()
                .map(e -> modelMapper.map(e, EmployeeDTO.class))
                .toList();
    }
}
