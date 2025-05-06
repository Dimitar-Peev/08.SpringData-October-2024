package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.xml.ApartmentSeedDto;
import softuni.exam.models.dto.xml.ApartmentSeedRootDto;
import softuni.exam.models.entity.Apartment;
import softuni.exam.repository.ApartmentRepository;
import softuni.exam.repository.TownRepository;
import softuni.exam.service.ApartmentService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import jakarta.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

@Service
public class ApartmentServiceImpl implements ApartmentService {

    private static final String FILE_PATH = "src/main/resources/files/xml/apartments.xml";
    private static final Locale locale = Locale.US;
    private static final DecimalFormat df = new DecimalFormat("####.00", DecimalFormatSymbols.getInstance(locale));

    private final ApartmentRepository apartmentRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final XmlParser xmlParser;
    private final TownRepository townRepository;

    @Autowired
    public ApartmentServiceImpl(ApartmentRepository apartmentRepository, ModelMapper modelMapper, ValidationUtil validationUtil, XmlParser xmlParser, TownRepository townRepository) {
        this.apartmentRepository = apartmentRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.xmlParser = xmlParser;
        this.townRepository = townRepository;
    }

    @Override
    public boolean areImported() {
        return this.apartmentRepository.count() > 0;
    }

    @Override
    public String readApartmentsFromFile() throws IOException {
        return Files.readString(Path.of(FILE_PATH));
    }

    @Override
    public String importApartments() throws IOException, JAXBException {
        StringBuilder output = new StringBuilder();

        ApartmentSeedRootDto apartmentSeedRootDto = this.xmlParser.convertFromFile(FILE_PATH, ApartmentSeedRootDto.class);

        apartmentSeedRootDto.getApartments()
                .stream()
                .filter(companySeedDto -> {
                    boolean isExist = this.apartmentRepository
                            .existsByTownTownNameAndArea(companySeedDto.getTown(),companySeedDto.getArea());

                    boolean isValid = validationUtil.isValid(companySeedDto) && !isExist;

                    output.append(isValid ? String.format("Successfully imported apartment %s - %s",
                                    companySeedDto.getApartmentType(), df.format(companySeedDto.getArea()))
                                    : "Invalid apartment")
                            .append(System.lineSeparator());
                    return isValid;
                })
                .map(this::newApartment)
                .forEach(apartmentRepository::save);

        return output.toString().trim();
    }

    private Apartment newApartment(ApartmentSeedDto apartmentSeedDto) {
        Apartment apartment = modelMapper.map(apartmentSeedDto, Apartment.class);
        apartment.setTown(this.townRepository.findByTownName(apartmentSeedDto.getTown()));
        return apartment;
    }
}
