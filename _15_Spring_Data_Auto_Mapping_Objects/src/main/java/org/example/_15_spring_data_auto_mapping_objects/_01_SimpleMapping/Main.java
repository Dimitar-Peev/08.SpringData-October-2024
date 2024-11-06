package org.example._15_spring_data_auto_mapping_objects._01_SimpleMapping;

import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Main {

    public static void main(String[] args) {
        ModelMapper mapper = new ModelMapper();

        Employee employee = new Employee("Dimitar", "Peev", new BigDecimal(5000), LocalDate.now(), "Ruse");

        EmployeeDTO map = mapper.map(employee, EmployeeDTO.class);
        System.out.println(map);

   }
}
