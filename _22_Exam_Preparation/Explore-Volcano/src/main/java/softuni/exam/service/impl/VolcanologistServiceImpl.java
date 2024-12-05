package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.xml.VolcanologistSeedDto;
import softuni.exam.models.dto.xml.VolcanologistSeedRootDto;
import softuni.exam.models.entity.Volcanologist;
import softuni.exam.repository.VolcanoRepository;
import softuni.exam.repository.VolcanologistRepository;
import softuni.exam.service.VolcanologistService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@Service
public class VolcanologistServiceImpl implements VolcanologistService {

    private static final String FILE_PATH = "src/main/resources/files/xml/volcanologists.xml";

    private final VolcanologistRepository volcanologistRepository;
    private final VolcanoRepository volcanoRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final XmlParser xmlParser;

    public VolcanologistServiceImpl(VolcanologistRepository volcanologistRepository, VolcanoRepository volcanoRepository, ModelMapper modelMapper, ValidationUtil validationUtil, XmlParser xmlParser) {
        this.volcanologistRepository = volcanologistRepository;
        this.volcanoRepository = volcanoRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.xmlParser = xmlParser;
    }

    @Override
    public boolean areImported() {
        return this.volcanologistRepository.count() > 0;
    }

    @Override
    public String readVolcanologistsFromFile() throws IOException {
        return Files.readString(Path.of(FILE_PATH));
    }

    @Override
    public String importVolcanologists() throws IOException, JAXBException {
        StringBuilder sb = new StringBuilder();

        VolcanologistSeedRootDto volcanologistSeedRootDto =
                this.xmlParser.fromFile(FILE_PATH, VolcanologistSeedRootDto.class);

        List<VolcanologistSeedDto> volcanologistSeedDtos = volcanologistSeedRootDto.getVolcanologistSeedDtos();

        for (VolcanologistSeedDto volcanologistSeedDto : volcanologistSeedDtos) {
            boolean isValid = this.validationUtil.isValid(volcanologistSeedDto);
            Optional<Volcanologist> byFirstNameAndLastName = this.volcanologistRepository
                    .findByFirstNameAndLastName(volcanologistSeedDto.getFirstName(), volcanologistSeedDto.getLastName());
            boolean isEmpty = this.volcanoRepository.findById(volcanologistSeedDto.getExploringVolcano()).isEmpty();

            if (!isValid || byFirstNameAndLastName.isPresent() || isEmpty) {
                sb.append("Invalid volcanologist").append(System.lineSeparator());
                continue;
            }

            Volcanologist volcanologist = this.modelMapper.map(volcanologistSeedDto, Volcanologist.class);
            volcanologist.setExploringVolcano(this.volcanoRepository.findById(volcanologistSeedDto.getExploringVolcano()).get());

            this.volcanologistRepository.saveAndFlush(volcanologist);

            sb.append(String.format("Successfully imported volcanologist %s %s",
                            volcanologist.getFirstName(), volcanologist.getLastName()))
                    .append(System.lineSeparator());

        }

        return sb.toString().trim();
    }
}