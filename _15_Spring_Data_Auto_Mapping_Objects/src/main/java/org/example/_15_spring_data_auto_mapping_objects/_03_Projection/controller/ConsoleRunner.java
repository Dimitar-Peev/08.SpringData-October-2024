package org.example._15_spring_data_auto_mapping_objects._03_Projection.controller;

import org.example._15_spring_data_auto_mapping_objects._03_Projection.entities.dtos.EmployeeDTO;
import org.example._15_spring_data_auto_mapping_objects._03_Projection.services.EmployeeService;
import org.example._15_spring_data_auto_mapping_objects._03_Projection.services.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

@Component
public class ConsoleRunner implements CommandLineRunner {
    private final EmployeeService employeeService;
    private final ManagerService managerService;

    @Autowired
    public ConsoleRunner(EmployeeService employeeService, ManagerService managerService) {
        this.employeeService = employeeService;
        this.managerService = managerService;
    }

    @Override
    public void run(String... args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter year: ");
        int bornBefore = Integer.parseInt(scanner.nextLine());

        List<EmployeeDTO> result = employeeService.findEmployeesBornBefore(bornBefore);
        System.out.println("Employees born before " + bornBefore + ":");
        result.forEach(System.out::println);

        List<EmployeeDTO> result2 = employeeService.findAllWithManager();
        System.out.println("Employees with manager:");
        result2.forEach(r -> System.out.printf("\t%s %s%n", r.getFirstName(), r.getLastName()));

        List<EmployeeDTO> result3 = managerService.findAllManagers();
        System.out.println("Managers:");
        result3.forEach(r -> System.out.printf("\t%s %s%n", r.getFirstName(), r.getLastName()));

//        CreateEmployeeDTO
//        employeeService.create()

    }
}
