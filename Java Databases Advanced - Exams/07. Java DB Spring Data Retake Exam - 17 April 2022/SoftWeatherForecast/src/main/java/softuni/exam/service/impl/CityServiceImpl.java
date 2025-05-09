package softuni.exam.service.impl;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.json.CityImportDto;
import softuni.exam.models.entity.City;
import softuni.exam.repository.CityRepository;
import softuni.exam.repository.CountryRepository;
import softuni.exam.service.CityService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

import static softuni.exam.constants.Messages.*;
import static softuni.exam.constants.Paths.CITIES_PATH;

@RequiredArgsConstructor
@Service
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final ValidationUtil validationUtil;

    @Override
    public boolean areImported() {
        return this.cityRepository.count() > 0;
    }

    @Override
    public String readCitiesFileContent() throws IOException {
        return Files.readString(CITIES_PATH);
    }

    @Override
    public String importCities() throws IOException {
        final StringBuilder sb = new StringBuilder();

        CityImportDto[] cityImportDtos = this.gson.fromJson(readCitiesFileContent(), CityImportDto[].class);

        final List<City> citiesList = Arrays.stream(cityImportDtos)
                .filter(cityImportDto -> {
                    boolean isValid = this.validationUtil.isValid(cityImportDto);

                    if (this.cityRepository.findFirstByCityName(cityImportDto.getCityName()).isPresent()) {
                        isValid = false;
                    }

                    if (isValid) {
                        sb.append(String.format(VALID_CITY, cityImportDto.getCityName(), cityImportDto.getPopulation()))
                                .append(System.lineSeparator());
                        if (countryRepository.findById(cityImportDto.getCountry()).isPresent()) {
                            City city = this.modelMapper.map(cityImportDto, City.class);
                            city.setCountry(countryRepository.findById(cityImportDto.getCountry()).get());

                            this.cityRepository.saveAndFlush(city);
                        } else {
                            sb.append("Error");
                        }

                    } else {
                        sb.append(INVALID_CITY).append(System.lineSeparator());
                    }

                    return isValid;
                })
                .map(cityImportDto -> this.modelMapper.map(cityImportDto, City.class))
                .toList();

        return sb.toString().trim();
    }
}
