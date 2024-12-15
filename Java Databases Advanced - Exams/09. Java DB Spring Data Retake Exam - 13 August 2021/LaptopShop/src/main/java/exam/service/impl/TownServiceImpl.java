package exam.service.impl;

import exam.model.dtos.xml.TownSeedRootDto;
import exam.model.entities.Town;
import exam.repository.TownRepository;
import exam.service.TownService;
import exam.util.ValidationUtil;
import exam.util.XmlParser;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import jakarta.xml.bind.JAXBException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class TownServiceImpl implements TownService {

    private static final String FILE_PATH = "src/main/resources/files/xml/towns.xml";

    private final TownRepository townRepository;
    private final XmlParser xmlParser;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;

    public TownServiceImpl(TownRepository townRepository, XmlParser xmlParser, ValidationUtil validationUtil, ModelMapper modelMapper) {
        this.townRepository = townRepository;
        this.xmlParser = xmlParser;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
    }

    @Override
    public boolean areImported() {
        return this.townRepository.count() > 0;
    }

    @Override
    public String readTownsFileContent() throws IOException {
        return Files.readString(Path.of(FILE_PATH));
    }

    @Override
    public String importTowns() throws FileNotFoundException, JAXBException {
        StringBuilder output = new StringBuilder();

        TownSeedRootDto townSeedRootDto = this.xmlParser.convertFromFile(FILE_PATH, TownSeedRootDto.class);

        townSeedRootDto
                .getTowns()
                .stream()
                .filter(townSeedDto -> {
                    boolean isValid = this.validationUtil.isValid(townSeedDto);

                    output.append(isValid ? "Successfully imported Town " + townSeedDto.getName()
                                    : "Invalid town")
                            .append(System.lineSeparator());

                    return isValid;
                })
                .map(townSeedDto -> this.modelMapper.map(townSeedDto, Town.class))
                .forEach(townRepository::save);

        return output.toString().trim();
    }

    @Override
    public Town findTownByName(String townName) {
        return this.townRepository.findByName(townName);
    }
}
