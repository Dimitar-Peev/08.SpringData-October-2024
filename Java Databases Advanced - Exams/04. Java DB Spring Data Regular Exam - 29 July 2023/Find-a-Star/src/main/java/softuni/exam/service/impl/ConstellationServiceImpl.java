package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.json.ConstellationSeedDto;
import softuni.exam.models.entity.Constellation;
import softuni.exam.repository.ConstellationRepository;
import softuni.exam.service.ConstellationService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Optional;

@Service
public class ConstellationServiceImpl implements ConstellationService {

    private static final String FILE_PATH = "src/main/resources/files/json/constellations.json";

    private final ConstellationRepository constellationRepository;
    private final Gson gson;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    @Autowired
    public ConstellationServiceImpl(ConstellationRepository constellationRepository, Gson gson, ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.constellationRepository = constellationRepository;
        this.gson = gson;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public boolean areImported() {
        return this.constellationRepository.count() > 0;
    }

    @Override
    public String readConstellationsFromFile() throws IOException {
        return new String(Files.readAllBytes(Path.of(FILE_PATH)));
    }

    @Override
    public String importConstellations() throws IOException {

        StringBuilder sb = new StringBuilder();

        ConstellationSeedDto[] constellationSeedDtos = gson.fromJson(readConstellationsFromFile(), ConstellationSeedDto[].class);

        Arrays.stream(constellationSeedDtos)
                .filter(constellation -> {
                    boolean isValid = validationUtil.isValid(constellation);

                    Optional<Constellation> byName = constellationRepository.findByName(constellation.getName());

                    if (byName.isPresent()) {
                        isValid = false;
                    }

                    sb.append(isValid ? String.format("Successfully imported constellation %s - %s",
                                    constellation.getName(), constellation.getDescription())
                                    : "Invalid constellation")
                            .append(System.lineSeparator());

                    return isValid;
                })
                .map(constellationSeedDto -> modelMapper.map(constellationSeedDto, Constellation.class))
                .forEach(constellationRepository::save);

        return sb.toString().trim();
    }
}
