package softuni.exam.service.impl;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.json.CountryImportDto;
import softuni.exam.models.entity.Country;
import softuni.exam.repository.CountryRepository;
import softuni.exam.service.CountryService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

import static softuni.exam.constants.Messages.INVALID_COUNTRY;
import static softuni.exam.constants.Messages.VALID_COUNTRY;
import static softuni.exam.constants.Paths.COUNTRIES_PATH;

@RequiredArgsConstructor
@Service
public class CountryServiceImpl implements CountryService {

    private final CountryRepository countryRepository;
    private final Gson gson;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;

    @Override
    public boolean areImported() {
        return this.countryRepository.count() > 0;
    }

    @Override
    public String readCountriesFromFile() throws IOException {
        return Files.readString(COUNTRIES_PATH);
    }

    @Override
    public String importCountries() throws IOException {
        final StringBuilder sb = new StringBuilder();

        CountryImportDto[] countryImportDtos = this.gson.fromJson(readCountriesFromFile(), CountryImportDto[].class);

        Arrays.stream(countryImportDtos)
                .forEach(countryImportDto -> {
                    boolean isValid = this.validationUtil.isValid(countryImportDto);

                    if (this.countryRepository.findFirstByCountryName(countryImportDto.getCountryName()).isPresent()) {
                        isValid = false;
                    }

                    if (isValid) {
                        sb.append(String.format(VALID_COUNTRY, countryImportDto.getCountryName(), countryImportDto.getCurrency()));
                        sb.append(System.lineSeparator());
                        this.countryRepository.saveAndFlush(this.modelMapper.map(countryImportDto, Country.class));
                    } else {
                        sb.append(INVALID_COUNTRY).append(System.lineSeparator());
                    }
                });

        return sb.toString().trim();
    }
}
