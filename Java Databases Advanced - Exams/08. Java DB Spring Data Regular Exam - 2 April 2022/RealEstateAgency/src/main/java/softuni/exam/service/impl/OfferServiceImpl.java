package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.xml.OfferSeedDto;
import softuni.exam.models.dto.xml.OfferSeedRootDto;
import softuni.exam.models.entity.ApartmentType;
import softuni.exam.models.entity.Offer;
import softuni.exam.repository.AgentRepository;
import softuni.exam.repository.ApartmentRepository;
import softuni.exam.repository.OfferRepository;
import softuni.exam.service.OfferService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import jakarta.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OfferServiceImpl implements OfferService {

    private static final String FILE_PATH = "src/main/resources/files/xml/offers.xml";

    private final OfferRepository offerRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final XmlParser xmlParser;
    private final AgentRepository agentRepository;
    private final ApartmentRepository apartmentRepository;

    @Autowired
    public OfferServiceImpl(OfferRepository offerRepository, ModelMapper modelMapper, ValidationUtil validationUtil, XmlParser xmlParser, AgentRepository agentRepository, ApartmentRepository apartmentRepository) {
        this.offerRepository = offerRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.xmlParser = xmlParser;
        this.agentRepository = agentRepository;
        this.apartmentRepository = apartmentRepository;
    }

    @Override
    public boolean areImported() {
        return this.offerRepository.count() > 0;
    }

    @Override
    public String readOffersFileContent() throws IOException {
        return Files.readString(Path.of(FILE_PATH));
    }

    @Override
    public String importOffers() throws IOException, JAXBException {
        StringBuilder output = new StringBuilder();

        OfferSeedRootDto offerSeedRootDto = this.xmlParser.convertFromFile(FILE_PATH, OfferSeedRootDto.class);

        offerSeedRootDto.getOffers()
                .stream()
                .filter(offerSeedDto -> {
                    boolean isExist = this.agentRepository.existsByFirstName(offerSeedDto.getAgent().getName());

                    boolean isValid = validationUtil.isValid(offerSeedDto) && isExist;

                    output.append(isValid ? "Successfully imported offer " + offerSeedDto.getPrice()
                                    : "Invalid offer")
                            .append(System.lineSeparator());
                    return isValid;
                })
                .map(this::newOffer)
                .forEach(offerRepository::save);

        return output.toString().trim();
    }

    private Offer newOffer(OfferSeedDto offerSeedDto) {
        Offer offer = modelMapper.map(offerSeedDto, Offer.class);
        offer.setAgent(this.agentRepository.findByFirstName(offerSeedDto.getAgent().getName()));
        offer.setApartment(this.apartmentRepository.findById(offerSeedDto.getApartment().getId()));
        return offer;
    }

    @Override
    public String exportOffers() {
        List<Offer> offerList = this.offerRepository
                .findByApartment_ApartmentTypeOrderByApartment_AreaDescPriceAsc(ApartmentType.valueOf("three_rooms"));

        return offerList
                .stream()
                .map(Offer::toString)
                .collect(Collectors.joining(""));
    }
}
