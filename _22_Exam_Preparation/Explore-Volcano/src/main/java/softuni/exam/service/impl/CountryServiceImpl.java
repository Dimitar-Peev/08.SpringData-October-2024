package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.json.CountrySeedDto;
import softuni.exam.models.entity.Country;
import softuni.exam.repository.CountryRepository;
import softuni.exam.service.CountryService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Service
public class CountryServiceImpl implements CountryService {

    private final CountryRepository countryRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final Gson gson;

    public CountryServiceImpl(CountryRepository countryRepository, ModelMapper modelMapper,
                              ValidationUtil validationUtil, Gson gson) {
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
    public String readCountriesFromFile() throws IOException {
        return Files.readString(Path.of("src/main/resources/files/json/countries.json"));
    }

    @Override
    public String importCountries() throws IOException {
        StringBuilder sb = new StringBuilder();

        CountrySeedDto[] countrySeedDtos = this.gson.fromJson(readCountriesFromFile(), CountrySeedDto[].class);

        for (CountrySeedDto countrySeedDTO : countrySeedDtos) {
            boolean isValid = this.validationUtil.isValid(countrySeedDTO);
            Optional<Country> countryOptional = this.countryRepository.findByName(countrySeedDTO.getName());

            if (!isValid || countryOptional.isPresent()) {
                sb.append("Invalid country").append(System.lineSeparator());
                continue;
            }

            Country country = this.modelMapper.map(countrySeedDTO, Country.class);
            this.countryRepository.saveAndFlush(country);

            sb.append(String.format("Successfully imported country %s - %s",
                    country.getName(), country.getCapital())).append(System.lineSeparator());
        }

        return sb.toString().trim();
    }

}
