package com.example.nltworkshop.service.impl;

import com.example.nltworkshop.data.repositories.CompanyRepository;
import com.example.nltworkshop.service.CompanyService;
import com.example.nltworkshop.service.model.imports.CompanyRootImportModel;
import com.example.nltworkshop.util.XmlParser;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class CompanyServiceImpl implements CompanyService {

    private static final String FILE_PATH = "src/main/resources/files/xmls/companies.xml";

    private final CompanyRepository companyRepository;
    private final ModelMapper modelMapper;
    private final XmlParser xmlParser;

    @Autowired
    public CompanyServiceImpl(CompanyRepository companyRepository, ModelMapper modelMapper, XmlParser xmlParser) {
        this.companyRepository = companyRepository;
        this.modelMapper = modelMapper;
        this.xmlParser = xmlParser;
    }

    @Override
    public boolean isImported() {
        return this.companyRepository.count() > 0;
    }

    //TODO DO IT WITH JACKSON, some errors are appearing
    //Already tested with JAXB
    @Override
    public void seedData() throws JAXBException, IOException {
        XmlMapper xmlMapper = new XmlMapper();
        CompanyRootImportModel companyRootImportModel = xmlMapper.readValue(readFile(), CompanyRootImportModel.class);
        System.out.println();

//        CompanyRootImportModel companyRootImportModel = this.xmlParser.fromFile(FILE_PATH, CompanyRootImportModel.class);
//        companyRootImportModel.getCompanyImportModels().forEach(company -> {
//            System.out.println();
//            this.companyRepository.saveAndFlush(this.modelMapper.map(company, Company.class));
//        });
    }

    @Override
    public String readFile() throws IOException {
        return Files.readString(Path.of(FILE_PATH));
    }
}
