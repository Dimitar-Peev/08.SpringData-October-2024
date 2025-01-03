package hiberspring.service.impl;

import hiberspring.domain.dtos.xml.ProductSeedDto;
import hiberspring.domain.dtos.xml.ProductSeedRootDto;
import hiberspring.domain.entities.Branch;
import hiberspring.domain.entities.Product;
import hiberspring.repository.ProductRepository;
import hiberspring.service.BranchService;
import hiberspring.service.ProductService;
import hiberspring.util.ValidationUtil;
import hiberspring.util.XmlParser;
import jakarta.xml.bind.JAXBException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static hiberspring.common.GlobalConstants.INCORRECT_DATA_MESSAGE;
import static hiberspring.common.GlobalConstants.PRODUCTS_FILE_PATH;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final BranchService branchService;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final XmlParser xmlParser;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, BranchService branchService, ModelMapper modelMapper, ValidationUtil validationUtil, XmlParser xmlParser) {
        this.productRepository = productRepository;
        this.branchService = branchService;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.xmlParser = xmlParser;
    }

    @Override
    public Boolean productsAreImported() {
        return this.productRepository.count() > 0;
    }

    @Override
    public String readProductsXmlFile() throws IOException {
        return Files.readString(Path.of(PRODUCTS_FILE_PATH));
    }

    @Override
    public String importProducts() throws JAXBException, IOException {
        StringBuilder output = new StringBuilder();

        ProductSeedRootDto productSeedRootDto = this.xmlParser.convertFromFile(PRODUCTS_FILE_PATH, ProductSeedRootDto.class);

        productSeedRootDto.getProducts()
                .stream()
                .filter(productSeedDto -> {
                    boolean isExist = this.productRepository.existsByName(productSeedDto.getName());

                    boolean isValid = this.validationUtil.isValid(productSeedDto) && !isExist;

                    output.append(isValid ? String.format("Successfully imported Product %s.", productSeedDto.getName())
                                    : INCORRECT_DATA_MESSAGE)
                            .append(System.lineSeparator());
                    return isValid;
                })
                .map(this::newProduct)
                .forEach(productRepository::save);

        return output.toString().trim();
    }

    private Product newProduct(ProductSeedDto productSeedDto) {
        Product product = this.modelMapper.map(productSeedDto, Product.class);
        Branch branch = this.branchService.getBranchByName(productSeedDto.getBranch());
        product.setBranch(branch);
        return product;
    }
}
