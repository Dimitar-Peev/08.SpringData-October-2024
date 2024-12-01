package softuni.exam.service.impl;

import jakarta.xml.bind.JAXBException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.xml.PersonalDataSeedRootDto;
import softuni.exam.models.entity.PersonalData;
import softuni.exam.repository.PersonalDataRepository;
import softuni.exam.service.PersonalDataService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class PersonalDataServiceImpl implements PersonalDataService {

    private static final String FILE_PATH = "src/main/resources/files/xml/personal_data.xml";

    private final PersonalDataRepository personalDataRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final XmlParser xmlParser;

    @Autowired
    public PersonalDataServiceImpl(PersonalDataRepository personalDataRepository, ModelMapper modelMapper,
                                   ValidationUtil validationUtil, XmlParser xmlParser) {
        this.personalDataRepository = personalDataRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.xmlParser = xmlParser;
    }

    @Override
    public boolean areImported() {
        return this.personalDataRepository.count() > 0;
    }

    @Override
    public String readPersonalDataFileContent() throws IOException {
        return Files.readString(Path.of(FILE_PATH));
    }

    @Override
    public String importPersonalData() throws JAXBException, FileNotFoundException {
        StringBuilder output = new StringBuilder();

        PersonalDataSeedRootDto personalDataSeedRootDto = this.xmlParser.convertFromFile(FILE_PATH, PersonalDataSeedRootDto.class);

        personalDataSeedRootDto.getPersonalDataSeedDtoList()
                .stream()
                .filter(personalDataSeedDto -> {

                    boolean isExist = personalDataRepository.existsByCardNumber(personalDataSeedDto.getCardNumber());

                    boolean isValid = validationUtil.isValid(personalDataSeedDto)
                            && !isExist;

                    output.append(isValid ? "Successfully imported personal data for visitor with card number "
                                    + personalDataSeedDto.getCardNumber()
                                    : "Invalid personal data")
                            .append(System.lineSeparator());
                    return isValid;
                })
                .map(personalDataSeedDto -> modelMapper.map(personalDataSeedDto, PersonalData.class))
                .forEach(personalDataRepository::save);

        return output.toString().trim();
    }
}
