package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.json.MechanicSeedDto;
import softuni.exam.models.entity.Mechanic;
import softuni.exam.repository.MechanicRepository;
import softuni.exam.service.MechanicService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@Service
public class MechanicServiceImpl implements MechanicService {

    private static final String MECHANICS_FILE_PATH = "src/main/resources/files/json/mechanics.json";

    private final MechanicRepository mechanicRepository;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;
    private final Gson gson;

    @Autowired
    public MechanicServiceImpl(MechanicRepository mechanicRepository, ValidationUtil validationUtil,
                               ModelMapper modelMapper, Gson gson) {
        this.mechanicRepository = mechanicRepository;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.gson = gson;
    }

    @Override
    public boolean areImported() {
        return this.mechanicRepository.count() > 0;
    }

    @Override
    public String readMechanicsFromFile() throws IOException {
        return Files.readString(Path.of(MECHANICS_FILE_PATH));
    }

    @Override
    public String importMechanics() throws IOException {
        StringBuilder output = new StringBuilder();

        MechanicSeedDto[] mechanicSeedDtos = this.gson.fromJson(readMechanicsFromFile(), MechanicSeedDto[].class);

        Arrays.stream(mechanicSeedDtos)
                .filter(mechanicSeedDto -> {
                    boolean isExistByEmail = this.mechanicRepository.existsByEmail(mechanicSeedDto.getEmail());

                    boolean isValid = this.validationUtil.isValid(mechanicSeedDto) && !isExistByEmail;

                    output.append(isValid
                                    ? String.format("Successfully imported mechanic %s %s",
                                    mechanicSeedDto.getFirstName(), mechanicSeedDto.getLastName())
                                    : "Invalid mechanic")
                            .append(System.lineSeparator());

                    return isValid;
                })
                .map(this::newMechanic)
                .forEach(mechanicRepository::save);

        return output.toString().trim();
    }

    private Mechanic newMechanic(MechanicSeedDto mechanicSeedDto) {
        return this.modelMapper.map(mechanicSeedDto, Mechanic.class);
    }
}
