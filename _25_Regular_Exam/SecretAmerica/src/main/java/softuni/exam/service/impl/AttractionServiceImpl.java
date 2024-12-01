package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.json.AttractionSeedDto;
import softuni.exam.models.entity.Attraction;
import softuni.exam.repository.AttractionRepository;
import softuni.exam.repository.CountryRepository;
import softuni.exam.service.AttractionService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

@Service
public class AttractionServiceImpl implements AttractionService {

    private static final String FILE_PATH = "src/main/resources/files/json/attractions.json";

    private final AttractionRepository attractionRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final Gson gson;
    private final CountryRepository countryRepository;

    @Autowired
    public AttractionServiceImpl(AttractionRepository attractionRepository, ModelMapper modelMapper,
                                 ValidationUtil validationUtil, Gson gson, CountryRepository countryRepository) {
        this.attractionRepository = attractionRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gson = gson;
        this.countryRepository = countryRepository;
    }

    @Override
    public boolean areImported() {
        return this.attractionRepository.count() > 0;
    }

    @Override
    public String readAttractionsFileContent() throws IOException {
        return Files.readString(Path.of(FILE_PATH));
    }

    @Override
    public String importAttractions() throws IOException {
        StringBuilder output = new StringBuilder();

        AttractionSeedDto[] attractionSeedDtos = this.gson.fromJson(readAttractionsFileContent(), AttractionSeedDto[].class);

        Arrays.stream(attractionSeedDtos)
                .filter(attractionSeedDto -> {

                    boolean isExist = this.attractionRepository.existsAttractionByName(attractionSeedDto.getName());

                    boolean isValid = this.validationUtil.isValid(attractionSeedDto) && !isExist;

                    output.append(isValid
                                    ? "Successfully imported attraction " + attractionSeedDto.getName()
                                    : "Invalid attraction")
                            .append(System.lineSeparator());

                    return isValid;
                })
                .map(this::newAttraction)
                .forEach(attractionRepository::save);

        return output.toString().trim();
    }

    private Attraction newAttraction(AttractionSeedDto attractionSeedDto) {
        Attraction attraction = this.modelMapper.map(attractionSeedDto, Attraction.class);

        attraction.setCountry(this.countryRepository.findCountryById(attractionSeedDto.getCountry()));

        return attraction;
    }

    @Override
    public String exportAttractions() {
        List<Attraction> attractions = this.attractionRepository
                .findHistoricalAndArchaeologicalSitesWithElevationAbove300();

        StringBuilder output = new StringBuilder();
        attractions.forEach(attraction -> {
            output.append(String.format(
                    "Attraction with ID%d:\n***%s - %s at an altitude of %dm. somewhere in %s.\n",
                    attraction.getId(),
                    attraction.getName(),
                    attraction.getDescription(),
                    attraction.getElevation(),
                    attraction.getCountry().getName()
            ));
        });
        return output.toString();
    }
}
