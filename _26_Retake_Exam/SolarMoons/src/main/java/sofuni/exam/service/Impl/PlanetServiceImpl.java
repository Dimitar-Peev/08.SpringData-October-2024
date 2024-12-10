package sofuni.exam.service.Impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sofuni.exam.models.dto.PlanetSeedDto;
import sofuni.exam.models.entity.Planet;
import sofuni.exam.repository.PlanetRepository;
import sofuni.exam.service.PlanetService;
import sofuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@Service
public class PlanetServiceImpl implements PlanetService {

    private static final String FILE_PATH = "src/main/resources/files/json/planets.json";

    private final PlanetRepository planetRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final Gson gson;

    @Autowired
    public PlanetServiceImpl(PlanetRepository planetRepository, ModelMapper modelMapper, ValidationUtil validationUtil, Gson gson) {
        this.planetRepository = planetRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gson = gson;
    }

    @Override
    public boolean areImported() {
        return this.planetRepository.count() > 0;
    }

    @Override
    public String readPlanetsFileContent() throws IOException {
        return Files.readString(Path.of(FILE_PATH));
    }

    @Override
    public String importPlanets() throws IOException {
        StringBuilder sb = new StringBuilder();

        PlanetSeedDto[] planetSeedDtos = this.gson.fromJson(readPlanetsFileContent(), PlanetSeedDto[].class);

        Arrays.stream(planetSeedDtos)
                .filter(planetSeedDto -> {

                    boolean isExistByName = this.planetRepository.existsByName(planetSeedDto.getName());

                    boolean isValid = this.validationUtil.isValid(planetSeedDto) && !isExistByName;

                    sb.append(isValid ? "Successfully imported planet " + planetSeedDto.getName()
                            : "Invalid planet").append(System.lineSeparator());

                    return isValid;
                })
                .map(this::newPlanet)
                .forEach(planetRepository::save);

        return sb.toString().trim();
    }

    private Planet newPlanet(PlanetSeedDto passengerSeedDto) {
        return modelMapper.map(passengerSeedDto, Planet.class);
    }
}
