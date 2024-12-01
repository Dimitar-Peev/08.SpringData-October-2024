package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.json.CountrySeedDto;
import softuni.exam.models.entity.Country;
import softuni.exam.repository.CountryRepository;
import softuni.exam.service.CountryService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@Service
public class CountryServiceImpl implements CountryService {

    private static final String FILE_PATH = "src/main/resources/files/json/countries.json";

    private final CountryRepository countryRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final Gson gson;

    @Autowired
    public CountryServiceImpl(CountryRepository countryRepository, ModelMapper modelMapper, ValidationUtil validationUtil, Gson gson) {
        this.countryRepository = countryRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gson = gson;
    }

    @Override
    public boolean areImported() {
        return this.countryRepository.count() > 0;
    }

    @Override
    public String readCountryFileContent() throws IOException {
        return Files.readString(Path.of(FILE_PATH));
    }

    @Override
    public String importCountries() throws IOException {
        StringBuilder output = new StringBuilder();

        CountrySeedDto[] countrySeedDtos = this.gson.fromJson(readCountryFileContent(), CountrySeedDto[].class);

        Arrays.stream(countrySeedDtos)
                .filter(countrySeedDto -> {

                    boolean isExist = this.countryRepository.existsCountryByName(countrySeedDto.getName());

                    boolean isValid = this.validationUtil.isValid(countrySeedDto) && !isExist;

                    output.append(isValid
                                    ? "Successfully imported country " + countrySeedDto.getName()
                                    : "Invalid country")
                            .append(System.lineSeparator());

                    return isValid;
                })
                .map(countrySeedDto -> modelMapper.map(countrySeedDto, Country.class))
                .forEach(countryRepository::save);

        return output.toString().trim();
    }
}
