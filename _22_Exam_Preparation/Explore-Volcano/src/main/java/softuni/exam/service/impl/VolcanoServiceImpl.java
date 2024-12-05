package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.json.VolcanoSeedDto;
import softuni.exam.models.entity.Volcano;
import softuni.exam.models.enums.VolcanoType;
import softuni.exam.repository.CountryRepository;
import softuni.exam.repository.VolcanoRepository;
import softuni.exam.service.VolcanoService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Service
public class VolcanoServiceImpl implements VolcanoService {

    private final VolcanoRepository volcanoRepository;
    private final CountryRepository countryRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final Gson gson;

    public VolcanoServiceImpl(VolcanoRepository volcanoRepository, CountryRepository countryRepository,
                              ModelMapper modelMapper, ValidationUtil validationUtil, Gson gson) {
        this.volcanoRepository = volcanoRepository;
        this.countryRepository = countryRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gson = gson;
    }

    @Override
    public boolean areImported() {
        return this.volcanoRepository.count() > 0;
    }

    @Override
    public String readVolcanoesFileContent() throws IOException {
        return Files.readString(Path.of("src/main/resources/files/json/volcanoes.json"));
    }

    @Override
    public String importVolcanoes() throws IOException {
        StringBuilder sb = new StringBuilder();

        VolcanoSeedDto[] volcanoSeedDtos = gson.fromJson(readVolcanoesFileContent(), VolcanoSeedDto[].class);

        for (VolcanoSeedDto volcanoSeedDto : volcanoSeedDtos) {
            boolean isValid = this.validationUtil.isValid(volcanoSeedDto);
            Optional<Volcano> countryOptional = this.volcanoRepository.findByName(volcanoSeedDto.getName());

            if (!isValid || countryOptional.isPresent()) {
                sb.append("Invalid volcano").append(System.lineSeparator());
                continue;
            }

            Volcano volcano = this.modelMapper.map(volcanoSeedDto, Volcano.class);

            volcano.setVolcanoType(VolcanoType.valueOf(volcanoSeedDto.getVolcanoType()));
            volcano.setCountry(this.countryRepository.findById((long) volcanoSeedDto.getCountry()).get());

            this.volcanoRepository.saveAndFlush(volcano);

            sb.append(String.format("Successfully imported volcano %s of type %s",
                            volcano.getName(), volcano.getVolcanoType()))
                    .append(System.lineSeparator());
        }

        return sb.toString().trim();
    }

    @Override
    public String exportVolcanoes() {
        StringBuilder sb = new StringBuilder();

        this.volcanoRepository
                .findAllByElevationAfterAndIsActiveAndLastEruptionIsNotNullOrderByElevationDesc(3000, true)
                .forEach(volcano -> {
                    sb.append(String.format("Volcano: %s\n" +
                                            "   *Located in: %s\n" +
                                            "   **Elevation: %d\n" +
                                            "   ***Last eruption on: %s",
                                    volcano.getName(),
                                    volcano.getCountry().getName(),
                                    volcano.getElevation(),
                                    volcano.getLastEruption()))
                            .append(System.lineSeparator());
                });

        return sb.toString().trim();
    }
}