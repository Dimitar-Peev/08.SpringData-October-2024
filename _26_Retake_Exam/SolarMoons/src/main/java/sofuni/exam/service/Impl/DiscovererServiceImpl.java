package sofuni.exam.service.Impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sofuni.exam.models.dto.DiscovererSeedDto;
import sofuni.exam.models.entity.Discoverer;
import sofuni.exam.repository.DiscovererRepository;
import sofuni.exam.service.DiscovererService;
import sofuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@Service
public class DiscovererServiceImpl implements DiscovererService {

    private static final String FILE_PATH = "src/main/resources/files/json/discoverers.json";

    private final DiscovererRepository discovererRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final Gson gson;

    @Autowired
    public DiscovererServiceImpl(DiscovererRepository discovererRepository, ModelMapper modelMapper, ValidationUtil validationUtil, Gson gson) {
        this.discovererRepository = discovererRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gson = gson;
    }

    @Override
    public boolean areImported() {
        return this.discovererRepository.count() > 0;
    }

    @Override
    public String readDiscovererFileContent() throws IOException {
        return Files.readString(Path.of(FILE_PATH));
    }

    @Override
    public String importDiscoverers() throws IOException {
        StringBuilder sb = new StringBuilder();

        DiscovererSeedDto[] discovererSeedDtos = this.gson.fromJson(readDiscovererFileContent(), DiscovererSeedDto[].class);

        Arrays.stream(discovererSeedDtos)
                .filter(discovererSeedDto -> {

                    boolean isExistByName = this.discovererRepository
                            .existsByFirstNameAndLastName(discovererSeedDto.getFirstName(), discovererSeedDto.getLastName());

                    boolean isValid = this.validationUtil.isValid(discovererSeedDto) && !isExistByName;

                    sb.append(isValid ? String.format("Successfully imported discoverer %s %s",
                            discovererSeedDto.getFirstName(), discovererSeedDto.getLastName())
                            : "Invalid discoverer").append(System.lineSeparator());

                    return isValid;
                })
                .map(this::newDiscoverer)
                .forEach(discovererRepository::save);

        return sb.toString().trim();
    }

    private Discoverer newDiscoverer(DiscovererSeedDto discovererSeedDto) {
        return modelMapper.map(discovererSeedDto, Discoverer.class);
    }
}
