package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.json.SaleImportDTO;
import softuni.exam.models.entity.Sale;
import softuni.exam.models.entity.Seller;
import softuni.exam.repository.SaleRepository;
import softuni.exam.repository.SellerRepository;
import softuni.exam.service.SaleService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@Service
public class SaleServiceImpl implements SaleService {

    private static final String FILE_PATH = "src/main/resources/files/json/sales.json";

    private final SaleRepository saleRepository;
    private final SellerRepository sellerRepository;
    private final Gson gson;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    public SaleServiceImpl(SaleRepository saleRepository, SellerRepository sellerRepository, Gson gson, ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.saleRepository = saleRepository;
        this.sellerRepository = sellerRepository;
        this.gson = gson;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public boolean areImported() {
        return this.saleRepository.count() > 0;
    }

    @Override
    public String readSalesFileContent() throws IOException {
        return Files.readString(Path.of(FILE_PATH));
    }

    @Override
    public String importSales() throws IOException {
        StringBuilder sb = new StringBuilder();

        SaleImportDTO[] saleImportDTOS = this.gson.fromJson(readSalesFileContent(), SaleImportDTO[].class);

        Arrays.stream(saleImportDTOS)
                .filter(saleImportDTO -> {
                    boolean isValid = this.validationUtil.isValid(saleImportDTO) &&
                            this.saleRepository.findByNumber(saleImportDTO.getNumber()).isEmpty();

                    sb.append(isValid ? "Successfully imported sale with number " + saleImportDTO.getNumber()
                                    : "Invalid sale")
                            .append(System.lineSeparator());

                    return isValid;
                })
                .map(this::newSale)
                .forEach(saleRepository::save);

        return sb.toString().trim();
    }

    private Sale newSale(SaleImportDTO saleImportDTO) {
        Sale sale = this.modelMapper.map(saleImportDTO, Sale.class);
        Seller seller = this.sellerRepository.findById(saleImportDTO.getSeller()).get();
        sale.setSeller(seller);
        return sale;
    }
}
