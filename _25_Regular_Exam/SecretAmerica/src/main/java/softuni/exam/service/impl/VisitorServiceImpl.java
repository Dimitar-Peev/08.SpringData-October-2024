package softuni.exam.service.impl;

import jakarta.xml.bind.JAXBException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.xml.VisitorSeedDto;
import softuni.exam.models.dto.xml.VisitorSeedRootDto;
import softuni.exam.models.entity.Visitor;
import softuni.exam.repository.AttractionRepository;
import softuni.exam.repository.CountryRepository;
import softuni.exam.repository.PersonalDataRepository;
import softuni.exam.repository.VisitorRepository;
import softuni.exam.service.VisitorService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class VisitorServiceImpl implements VisitorService {

    private static final String FILE_PATH = "src/main/resources/files/xml/visitors.xml";

    private final VisitorRepository visitorRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final XmlParser xmlParser;
    private final AttractionRepository attractionRepository;
    private final CountryRepository countryRepository;
    private final PersonalDataRepository personalDataRepository;

    @Autowired
    public VisitorServiceImpl(VisitorRepository visitorRepository, ModelMapper modelMapper, ValidationUtil validationUtil, XmlParser xmlParser, AttractionRepository attractionRepository, CountryRepository countryRepository, PersonalDataRepository personalDataRepository) {
        this.visitorRepository = visitorRepository;

        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.xmlParser = xmlParser;
        this.attractionRepository = attractionRepository;
        this.countryRepository = countryRepository;
        this.personalDataRepository = personalDataRepository;
    }

    @Override
    public boolean areImported() {
        return this.visitorRepository.count() > 0;
    }

    @Override
    public String readVisitorsFileContent() throws IOException {
        return Files.readString(Path.of(FILE_PATH));
    }

    @Override
    public String importVisitors() throws JAXBException, FileNotFoundException {
        StringBuilder output = new StringBuilder();

        VisitorSeedRootDto visitorSeedRootDto = this.xmlParser.convertFromFile(FILE_PATH, VisitorSeedRootDto.class);

        visitorSeedRootDto.getVisitorSeedDtoList()
                .stream()
                .filter(visitorSeedDto -> {

                    boolean isExistByName = visitorRepository
                            .existsByFirstNameAndLastName(visitorSeedDto.getFirstName(), visitorSeedDto.getLastName());

                    boolean isExistByData = visitorRepository
                            .existsVisitorByPersonalDataId(visitorSeedDto.getPersonalDataId());

                    boolean isValid = validationUtil.isValid(visitorSeedDto)
                            && !isExistByName
                            && !isExistByData;

                    output.append(isValid ? String.format("Successfully imported visitor %s %s",
                                    visitorSeedDto.getFirstName(), visitorSeedDto.getLastName())
                                    : "Invalid visitor")
                            .append(System.lineSeparator());
                    return isValid;
                })
                .map(this::newVisitor)
                .forEach(visitorRepository::save);

        return output.toString().trim();
    }

    private Visitor newVisitor(VisitorSeedDto visitorSeedDto) {
        Visitor visitor = modelMapper.map(visitorSeedDto, Visitor.class);

        visitor.setAttraction(attractionRepository.findById(visitorSeedDto.getAttractionId()));
        visitor.setCountry(countryRepository.findCountryById(visitorSeedDto.getCountryId()));
        visitor.setPersonalData(personalDataRepository.findPersonalDataById(visitorSeedDto.getPersonalDataId()));

        return visitor;
    }
}
