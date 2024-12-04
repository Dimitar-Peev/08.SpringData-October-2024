package com.example.nltworkshop.service.impl;

import com.example.nltworkshop.data.entities.Employee;
import com.example.nltworkshop.data.entities.Project;
import com.example.nltworkshop.data.repositories.EmployeeRepository;
import com.example.nltworkshop.data.repositories.ProjectRepository;
import com.example.nltworkshop.service.EmployeeService;
import com.example.nltworkshop.service.model.imports.EmployeeImportModel;
import com.example.nltworkshop.service.model.imports.EmployeeRootImportModel;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private static final String FILE_PATH = "src/main/resources/files/xmls/employees.xml";

    private final ModelMapper modelMapper;
    private final XmlMapper xmlMapper;
    private final ProjectRepository projectRepository;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeServiceImpl(ModelMapper modelMapper, XmlMapper xmlMapper, ProjectRepository projectRepository,
                               EmployeeRepository employeeRepository) {
        this.modelMapper = modelMapper;
        this.xmlMapper = xmlMapper;
        this.projectRepository = projectRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public boolean isImported() {
        return this.employeeRepository.count() > 0;
    }

    @Override
    public void seedData() throws IOException {
        EmployeeRootImportModel employeeRootImportModel =
                this.xmlMapper.readValue(readFile(), EmployeeRootImportModel.class);

        employeeRootImportModel.getEmployees()
                .forEach(this::saveToDB);
    }

    private String saveToDB(EmployeeImportModel employeeImportModel) {
        Employee forDB = this.modelMapper.map(employeeImportModel, Employee.class);

        Optional<Project> project = this.projectRepository.findByName(employeeImportModel.getProject().getName());

        if (project.isEmpty()) {
            return "Invalid Employee. Project not found.";
        }

        forDB.setProject(project.get());
        this.employeeRepository.save(forDB);

        return "Successfully imported Employee.";
    }

    @Override
    public String readFile() throws IOException {
        return Files.readString(Path.of(FILE_PATH));
    }

    @Override
    @Transactional
    public String getEmployeesAbove25() {
        List<Employee> byAgeGreaterThan = this.employeeRepository.findByAgeGreaterThan(25);

        return byAgeGreaterThan
                .stream()
                .map(Objects::toString)
                .collect(Collectors.joining(System.lineSeparator()));
    }
}