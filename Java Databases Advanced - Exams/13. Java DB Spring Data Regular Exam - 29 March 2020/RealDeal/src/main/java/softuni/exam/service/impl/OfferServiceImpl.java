package softuni.exam.service.impl;

import jakarta.xml.bind.JAXBException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.xml.OfferSeedDto;
import softuni.exam.models.dto.xml.OfferSeedRootDto;
import softuni.exam.models.entity.Car;
import softuni.exam.models.entity.Offer;
import softuni.exam.models.entity.Seller;
import softuni.exam.repository.OfferRepository;
import softuni.exam.service.CarService;
import softuni.exam.service.OfferService;
import softuni.exam.service.SellerService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;

@AllArgsConstructor
@Service
public class OfferServiceImpl implements OfferService {

    private static final String FILE_PATH = "src/main/resources/files/xml/offers.xml";

    private final OfferRepository offerRepository;
    private final ModelMapper modelMapper;
    private final XmlParser xmlParser;
    private final ValidationUtil validationUtil;
    private final CarService carService;
    private final SellerService sellerService;

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
        StringBuilder sb = new StringBuilder();

        OfferSeedRootDto offerSeedRootDto = this.xmlParser.convertFromFile(FILE_PATH, OfferSeedRootDto.class);

        for (OfferSeedDto offerSeedDto : offerSeedRootDto.getOffers()) {

            if (this.validationUtil.isValid(offerSeedDto)) {
                Offer offer = this.modelMapper.map(offerSeedDto, Offer.class);
                Car car = carService.findById(offerSeedDto.getCar().getId());
                Seller seller = sellerService.findById(offerSeedDto.getSeller().getId());

                offer.setCar(car);
                offer.setSeller(seller);
                offer.setPictures(new HashSet<>(car.getPictures()));

                this.offerRepository.save(offer);
                sb.append(String.format("Successfully import offer %s - %b", offer.getAddedOn(), offer.getHasGoldStatus()));
            } else {
                sb.append("Invalid offer");
            }

            sb.append(System.lineSeparator());
        }

        return sb.toString().trim();
    }
}
