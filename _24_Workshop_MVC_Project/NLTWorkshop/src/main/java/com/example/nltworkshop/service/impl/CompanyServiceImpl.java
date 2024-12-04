package com.example.nltworkshop.service.impl;

import com.example.nltworkshop.data.entities.Company;
import com.example.nltworkshop.data.repositories.CompanyRepository;
import com.example.nltworkshop.service.CompanyService;
import com.example.nltworkshop.service.model.imports.CompanyRootImportModel;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class CompanyServiceImpl implements CompanyService {

    private static final String FILE_PATH = "src/main/resources/files/xmls/companies.xml";

    private final CompanyRepository companyRepository;
    private final ModelMapper modelMapper;
    private final XmlMapper xmlMapper;

    @Autowired
    public CompanyServiceImpl(CompanyRepository companyRepository, ModelMapper modelMapper, XmlMapper xmlMapper) {
        this.companyRepository = companyRepository;
        this.modelMapper = modelMapper;
        this.xmlMapper = xmlMapper;
    }

    @Override
    public boolean isImported() {
        return this.companyRepository.count() > 0;
    }


    @Override
    public void seedData() throws IOException {
        CompanyRootImportModel companyRootImportModel = this.xmlMapper.readValue(readFile(), CompanyRootImportModel.class);

        companyRootImportModel.getCompanyImportModels().forEach(company -> {
            System.out.println();
            Company toPersist = this.modelMapper.map(company, Company.class);
            this.companyRepository.saveAndFlush(toPersist);
        });
    }

    @Override
    public String readFile() throws IOException {
        return Files.readString(Path.of(FILE_PATH));
    }
}
