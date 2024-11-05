package org.example._15_spring_data_auto_mapping_objects._02_AdvancedMapping;

import org.example._15_spring_data_auto_mapping_objects._02_AdvancedMapping.dtos.ManagerDTO;
import org.example._15_spring_data_auto_mapping_objects._02_AdvancedMapping.entities.Employee;
import org.modelmapper.ModelMapper;

import java.util.List;

public class EmployeeMain {
    public static void main(String[] args) {
        Employee first = new Employee("Stephen", "Bjorn", 4300);
        Employee second = new Employee("Kirilyc", "Lefi", 4400);
        Employee third = new Employee("Jurgen", "Straus", 1000.45);
        Employee fourth = new Employee("Moni", "Kozinac", 2030.99);
        Employee fifth = new Employee("Kopp", "Spidok", 2000.21);

        Employee managerOne = new Employee("Steve", "Jobbsen", 5200, List.of(first, second));
        Employee managerTwo = new Employee("Carl", "Kormac", 6200, List.of(third,fourth,fifth));

        ModelMapper mapper = new ModelMapper();

        ManagerDTO oneDTO = mapper.map(managerOne, ManagerDTO.class);
        ManagerDTO twoDTO = mapper.map(managerTwo, ManagerDTO.class);

        System.out.println(oneDTO);
        System.out.println(twoDTO);
    }
}
