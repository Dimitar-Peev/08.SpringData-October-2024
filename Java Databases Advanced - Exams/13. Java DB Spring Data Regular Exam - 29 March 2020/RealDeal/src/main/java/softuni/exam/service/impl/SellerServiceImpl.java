package softuni.exam.service.impl;

import jakarta.xml.bind.JAXBException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.xml.SellerSeedDto;
import softuni.exam.models.dto.xml.SellerSeedRootDto;
import softuni.exam.models.entity.Seller;
import softuni.exam.repository.SellerRepository;
import softuni.exam.service.SellerService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@AllArgsConstructor
@Service
public class SellerServiceImpl implements SellerService {

    private static final String FILE_PATH = "src/main/resources/files/xml/sellers.xml";

    private final SellerRepository sellerRepository;
    private final ModelMapper modelMapper;
    private final XmlParser xmlParser;
    private final ValidationUtil validationUtil;

    @Override
    public boolean areImported() {
        return this.sellerRepository.count() > 0;
    }

    @Override
    public String readSellersFromFile() throws IOException {
        return Files.readString(Path.of(FILE_PATH));
    }

    @Override
    public String importSellers() throws IOException, JAXBException {
        StringBuilder sb = new StringBuilder();

        SellerSeedRootDto sellerSeedRootDto = this.xmlParser.convertFromFile(FILE_PATH, SellerSeedRootDto.class);

        for (SellerSeedDto sellerSeedDto : sellerSeedRootDto.getSellers()) {

            Seller seller = this.modelMapper.map(sellerSeedDto, Seller.class);

            if (this.validationUtil.isValid(sellerSeedDto) && seller.getRating() != null) {
                this.sellerRepository.save(seller);
                sb.append(String.format("Successfully import seller %s - %s", seller.getLastName(), seller.getEmail()));
            } else {
                sb.append("Invalid seller");
            }

            sb.append(System.lineSeparator());
        }

        return sb.toString().trim();
    }

    @Override
    public Seller findById(long id) {
        return this.sellerRepository.findById(id).get();
    }
}
