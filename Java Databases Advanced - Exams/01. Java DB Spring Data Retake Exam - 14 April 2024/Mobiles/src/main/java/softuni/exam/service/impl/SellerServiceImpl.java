package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.json.SellerImportDTO;
import softuni.exam.models.entity.Seller;
import softuni.exam.repository.SellerRepository;
import softuni.exam.service.SellerService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@Service
public class SellerServiceImpl implements SellerService {

    private static final String FILE_PATH = "src/main/resources/files/json/sellers.json";

    private final SellerRepository sellerRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final Gson gson;

    public SellerServiceImpl(SellerRepository sellerRepository, ModelMapper modelMapper, ValidationUtil validationUtil, Gson gson) {
        this.sellerRepository = sellerRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gson = gson;
    }

    @Override
    public boolean areImported() {
        return this.sellerRepository.count() > 0;
    }

    @Override
    public String readSellersFromFile() throws IOException {
        return Files.readString(Path.of(FILE_PATH));
    }

    @Override
    public String importSellers() throws IOException {
        StringBuilder sb = new StringBuilder();

        SellerImportDTO[] sellerImportDTOS = this.gson.fromJson(readSellersFromFile(), SellerImportDTO[].class);

        Arrays.stream(sellerImportDTOS)
                .filter(sellerImportDTO -> {
                    boolean isExist = this.sellerRepository.existsByLastName(sellerImportDTO.getLastName());

                    boolean isValid = this.validationUtil.isValid(sellerImportDTO) && !isExist;

                    sb.append(isValid
                                    ? String.format("Successfully imported seller %s %s",
                                    sellerImportDTO.getFirstName(), sellerImportDTO.getLastName())
                                    : "Invalid seller")
                            .append(System.lineSeparator());

                    return isValid;
                })
                .map(sellerImportDTO -> this.modelMapper.map(sellerImportDTO, Seller.class))
                .forEach(sellerRepository::save);

        return sb.toString().trim();
    }
}