package com.example.productsshop.service.impl;

import com.example.productsshop.data.entities.Category;
import com.example.productsshop.data.entities.Product;
import com.example.productsshop.data.entities.User;
import com.example.productsshop.data.repositories.CategoryRepository;
import com.example.productsshop.data.repositories.ProductRepository;
import com.example.productsshop.data.repositories.UserRepository;
import com.example.productsshop.service.ProductService;
import com.example.productsshop.service.dtos.ProductSeedJsonDto;
import com.example.productsshop.service.dtos.export.ProductExportJsonDto;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private static final String JSON_PATH = "src/main/resources/json/products.json";

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final Gson gson;

    public ProductServiceImpl(ProductRepository productRepository, UserRepository userRepository, CategoryRepository categoryRepository, ModelMapper modelMapper, Gson gson) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
        this.gson = gson;
    }

    @Override
    public void seedProducts() throws IOException {

        ProductSeedJsonDto[] productSeedJsonDtos = this.gson
                .fromJson(String.join("", Files.readAllLines(Path.of(JSON_PATH))), ProductSeedJsonDto[].class);

        for (ProductSeedJsonDto productSeedJsonDto : productSeedJsonDtos) {
            Product product = this.modelMapper.map(productSeedJsonDto, Product.class);
            product.setSeller(getRandomUser(true));
            if (product.getPrice().compareTo(BigDecimal.valueOf(600L)) > 0) {
                product.setBuyer(getRandomUser(false));
            }
            product.setCategories(getRandomCategories());

            this.productRepository.saveAndFlush(product);
        }

    }

    private Set<Category> getRandomCategories() {
        Set<Category> categories = new HashSet<>();
        int count = ThreadLocalRandom.current().nextInt(1, 4);

        for (int i = 0; i < count; i++) {
            categories.add(this.categoryRepository.findById(ThreadLocalRandom
                    .current().nextInt(1, (int) this.categoryRepository.count() + 1)).get());
        }
        return categories;
    }

    private User getRandomUser(boolean flag) {
        int randomId = 0;
        if (flag) {
            randomId = ThreadLocalRandom.current()
                    .nextInt(1, (int) this.userRepository.count() + 1);
        } else {
            randomId = ThreadLocalRandom.current()
                    .nextInt(1, (int) this.userRepository.count());
        }
        return this.userRepository.findById(randomId).orElse(null);
    }

    @Override
    public boolean isImported() {
        return this.productRepository.count() > 0;
    }

    @Override
    public void exportProductsInRange() throws IOException {

        Set<Product> allByPriceBetweenAndBuyerIsNull = this.productRepository
                .findAllByPriceBetweenAndBuyerIsNull(BigDecimal.valueOf(500L), BigDecimal.valueOf(1000L));

        Set<ProductExportJsonDto> collected = allByPriceBetweenAndBuyerIsNull
                .stream()
                .map(p -> {
                    ProductExportJsonDto product = this.modelMapper.map(p, ProductExportJsonDto.class);
                    product.setSeller(p.getSeller().getFirstName() + " " + p.getSeller().getLastName());
                    return product;
                })
                .collect(Collectors.toSet());

        String json = this.gson.toJson(collected);

        FileWriter writer = new FileWriter("src/main/resources/json/exports/products-in-range.json");
        writer.write(json);
        writer.close();
    }
}
