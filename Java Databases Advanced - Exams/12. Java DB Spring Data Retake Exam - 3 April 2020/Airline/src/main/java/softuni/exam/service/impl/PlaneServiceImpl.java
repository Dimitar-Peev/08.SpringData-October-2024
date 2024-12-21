package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dtos.xml.PlaneSeedRootDto;
import softuni.exam.models.entities.Plane;
import softuni.exam.repository.PlaneRepository;
import softuni.exam.service.PlaneService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import jakarta.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class PlaneServiceImpl implements PlaneService {

    private static final String FILE_PATH = "src/main/resources/files/xml/planes.xml";

    private final PlaneRepository planeRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final XmlParser xmlParser;

    @Autowired
    public PlaneServiceImpl(PlaneRepository planeRepository, ModelMapper modelMapper, ValidationUtil validationUtil, XmlParser xmlParser) {
        this.planeRepository = planeRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.xmlParser = xmlParser;
    }

    @Override
    public boolean areImported() {
        return this.planeRepository.count() > 0;
    }

    @Override
    public String readPlanesFileContent() throws IOException {
        return Files.readString(Path.of(FILE_PATH));
    }

    @Override
    public String importPlanes() throws IOException, JAXBException {
        StringBuilder output = new StringBuilder();

        PlaneSeedRootDto planeSeedRootDto = this.xmlParser.convertFromFile(FILE_PATH, PlaneSeedRootDto.class);

        planeSeedRootDto
                .getPlanes()
                .stream()
                .filter(sellerSeedDto -> {
                    boolean isValid = this.validationUtil.isValid(sellerSeedDto);
                    output.append(isValid ? String.format("Successfully imported Plane %s", sellerSeedDto.getRegisterNumber())
                                    : "Invalid Plane")
                            .append(System.lineSeparator());
                    return isValid;
                })
                .map(planeSeedDto -> this.modelMapper.map(planeSeedDto, Plane.class))
                .forEach(planeRepository::save);

        return output.toString().trim();
    }
}
