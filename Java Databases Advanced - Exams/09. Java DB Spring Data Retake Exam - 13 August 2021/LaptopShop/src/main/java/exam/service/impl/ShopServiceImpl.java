package exam.service.impl;

import exam.model.dtos.xml.ShopSeedDto;
import exam.model.dtos.xml.ShopSeedRootDto;
import exam.model.entities.Shop;
import exam.repository.ShopRepository;
import exam.service.ShopService;
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
public class ShopServiceImpl implements ShopService {

    private static final String FILE_PATH = "src/main/resources/files/xml/shops.xml";

    private final ShopRepository shopRepository;
    private final XmlParser xmlParser;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;
    private final TownService townService;

    public ShopServiceImpl(ShopRepository shopRepository, XmlParser xmlParser, ValidationUtil validationUtil,
                           ModelMapper modelMapper, TownService townService) {
        this.shopRepository = shopRepository;
        this.xmlParser = xmlParser;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.townService = townService;
    }

    @Override
    public boolean areImported() {
        return this.shopRepository.count() > 0;
    }

    @Override
    public String readShopsFileContent() throws IOException {
        return Files.readString(Path.of(FILE_PATH));
    }

    @Override
    public String importShops() throws FileNotFoundException, JAXBException {
        StringBuilder sb = new StringBuilder();

        ShopSeedRootDto shopSeedRootDto = this.xmlParser.convertFromFile(FILE_PATH, ShopSeedRootDto.class);

        shopSeedRootDto
                .getShops()
                .stream()
                .filter(shopSeedDto -> {
                    boolean isValid = this.validationUtil.isValid(shopSeedDto)
                            && this.shopRepository.findByName(shopSeedDto.getName()) == null;

                    sb.append(isValid ? String.format("Successfully imported Shop %s - %d ",
                                    shopSeedDto.getName(), shopSeedDto.getIncome())
                                    : "Invalid shop")
                            .append(System.lineSeparator());

                    return isValid;
                })
                .map(this::newShop)
                .forEach(shopRepository::save);

        return sb.toString().trim();
    }

    private Shop newShop(ShopSeedDto shopSeedDto) {
        Shop shop = this.modelMapper.map(shopSeedDto, Shop.class);
        shop.setTown(this.townService.findTownByName(shopSeedDto.getTown().getName()));
        return shop;
    }
}
