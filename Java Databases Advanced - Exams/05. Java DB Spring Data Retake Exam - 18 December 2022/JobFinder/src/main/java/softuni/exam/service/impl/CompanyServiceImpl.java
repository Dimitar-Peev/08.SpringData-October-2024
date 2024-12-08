package softuni.exam.service.impl;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.xml.CompanySeedDto;
import softuni.exam.models.dto.xml.CompanySeedRootDto;
import softuni.exam.models.entity.Company;
import softuni.exam.models.entity.Country;
import softuni.exam.repository.CompanyRepository;
import softuni.exam.repository.CountryRepository;
import softuni.exam.service.CompanyService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class CompanyServiceImpl implements CompanyService {

    private static final String FILE_PATH = "src/main/resources/files/xml/companies.xml";

    private final CompanyRepository companyRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final CountryRepository countryRepository;
    private final XmlMapper xmlMapper;

    @Autowired
    public CompanyServiceImpl(CompanyRepository companyRepository, ModelMapper modelMapper,
                              ValidationUtil validationUtil, CountryRepository countryRepository, XmlMapper xmlMapper) {
        this.companyRepository = companyRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.countryRepository = countryRepository;
        this.xmlMapper = xmlMapper;
    }

    @Override
    public boolean areImported() {
        return this.companyRepository.count() > 0;
    }

    @Override
    public String readCompaniesFromFile() throws IOException {
        return Files.readString(Path.of(FILE_PATH));
    }

    @Override
    public String importCompanies() throws IOException {
        StringBuilder output = new StringBuilder();

        CompanySeedRootDto companySeedRootDto = this.xmlMapper.readValue(readCompaniesFromFile(), CompanySeedRootDto.class);

        companySeedRootDto.getCompanies()
                .stream()
                .filter(companySeedDto -> {
                    boolean existsByName = this.companyRepository.existsByName(companySeedDto.getCompanyName());

                    boolean isValid = this.validationUtil.isValid(companySeedDto) && !existsByName;

                    output.append(isValid ? String.format("Successfully imported company %s - %d",
                                    companySeedDto.getCompanyName(), companySeedDto.getCountryId())
                                    : "Invalid company")
                            .append(System.lineSeparator());
                    return isValid;
                })
                .map(this::newCompany)
                .forEach(companyRepository::save);

        return output.toString().trim();
    }

    private Company newCompany(CompanySeedDto companySeedDto) {
        Company company = this.modelMapper.map(companySeedDto, Company.class);

        Country country = this.countryRepository.findCountryById(companySeedDto.getCountryId());
        company.setCountry(country);

        return company;
    }
}
