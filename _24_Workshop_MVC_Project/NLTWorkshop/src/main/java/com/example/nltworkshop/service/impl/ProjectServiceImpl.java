package com.example.nltworkshop.service.impl;

import com.example.nltworkshop.data.entities.Company;
import com.example.nltworkshop.data.entities.Project;
import com.example.nltworkshop.data.repositories.CompanyRepository;
import com.example.nltworkshop.data.repositories.ProjectRepository;
import com.example.nltworkshop.service.ProjectService;
import com.example.nltworkshop.service.model.imports.ProjectImportModel;
import com.example.nltworkshop.service.model.imports.ProjectRootImportModel;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {

    private static final String FILE_PATH = "src/main/resources/files/xmls/projects.xml";

    private final ProjectRepository projectRepository;
    private final CompanyRepository companyRepository;
    private final ModelMapper modelMapper;
    private final XmlMapper xmlMapper;

    @Autowired
    public ProjectServiceImpl(ProjectRepository projectRepository, CompanyRepository companyRepository,
                              ModelMapper modelMapper, XmlMapper xmlMapper) {
        this.projectRepository = projectRepository;
        this.companyRepository = companyRepository;
        this.modelMapper = modelMapper;
        this.xmlMapper = xmlMapper;
    }

    @Override
    public boolean isImported() {
        return this.projectRepository.count() > 0;
    }

    @Override
    public void seedData() throws IOException {
        ProjectRootImportModel projectRootImportModel =
                this.xmlMapper.readValue(readFile(), ProjectRootImportModel.class);

        projectRootImportModel.getProjectImportModels()
                .forEach(this::saveToDB);
    }

    private String saveToDB(ProjectImportModel project) {
        Project forDB = this.modelMapper.map(project, Project.class);
//        forDB.setStartDate(LocalDate.parse(project.getStartDate())); // instead of in modelMapper

        Optional<Company> company = this.companyRepository.findByName(forDB.getCompany().getName());

        if (company.isEmpty()) {
            return "Invalid project. Company not found.";
        }

        forDB.setCompany(company.get());
        this.projectRepository.save(forDB);

        return "Successfully imported project.";
    }


    @Override
    public String readFile() throws IOException {
        return Files.readString(Path.of(FILE_PATH));
    }

    @Override
    public String getFinishedProjects() {
        List<Project> projectList = this.projectRepository.findByIsFinishedTrue();

        return projectList
                .stream()
                .map(Object::toString)
                .collect(Collectors.joining(System.lineSeparator()));
    }
}