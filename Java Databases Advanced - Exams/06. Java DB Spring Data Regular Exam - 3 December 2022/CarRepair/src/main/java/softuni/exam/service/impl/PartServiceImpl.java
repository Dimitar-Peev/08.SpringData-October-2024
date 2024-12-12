package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.json.PartSeedDto;
import softuni.exam.models.entity.Part;
import softuni.exam.repository.PartRepository;
import softuni.exam.service.PartService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@Service
public class PartServiceImpl implements PartService {

    private static final String PARTS_FILE_PATH = "src/main/resources/files/json/parts.json";

    private final PartRepository partRepository;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;
    private final Gson gson;

    @Autowired
    public PartServiceImpl(PartRepository partRepository, ValidationUtil validationUtil, ModelMapper modelMapper, Gson gson) {
        this.partRepository = partRepository;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.gson = gson;
    }

    @Override
    public boolean areImported() {
        return this.partRepository.count() > 0;
    }

    @Override
    public String readPartsFileContent() throws IOException {
        return Files.readString(Path.of(PARTS_FILE_PATH));
    }

    @Override
    public String importParts() throws IOException {
        StringBuilder output = new StringBuilder();

        PartSeedDto[] partSeedDtos = this.gson.fromJson(readPartsFileContent(), PartSeedDto[].class);

        Arrays.stream(partSeedDtos)
                .filter(partSeedDto -> {
                    boolean isExistByName = this.partRepository.existsByPartName(partSeedDto.getPartName());

                    boolean isValid = this.validationUtil.isValid(partSeedDto) && !isExistByName;

                    output.append(isValid
                                    ? String.format("Successfully imported part %s - %s",
                                    partSeedDto.getPartName(), partSeedDto.getPrice())
                                    : "Invalid part")
                            .append(System.lineSeparator());

                    return isValid;
                })
                .map(this::newPart)
                .forEach(partRepository::save);

        return output.toString().trim();
    }

    private Part newPart(PartSeedDto partSeedDto) {
        return this.modelMapper.map(partSeedDto, Part.class);
    }
}
