package com.example.productsshop.service.impl;

import com.example.productsshop.data.entities.Category;
import com.example.productsshop.data.repositories.CategoryRepository;
import com.example.productsshop.service.CategoryService;
import com.example.productsshop.service.dtos.CategorySeedJsonDto;
import com.example.productsshop.service.dtos.export.CategoryExportsonDto;
import com.example.productsshop.utils.ValidationUtil;
import com.google.gson.Gson;
import jakarta.validation.ConstraintViolation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private static final String JSON_PATH = "src/main/resources/json/categories.json";

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final ValidationUtil validationUtil;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper modelMapper, Gson gson, ValidationUtil validationUtil) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.validationUtil = validationUtil;
    }

    @Override
    public void seedCategories() throws IOException {

        String jsonString = String.join("", Files.readAllLines(Path.of(JSON_PATH)));

        CategorySeedJsonDto[] categorySeedJsonDtos = this.gson.fromJson(jsonString, CategorySeedJsonDto[].class);

        for (CategorySeedJsonDto categorySeedJsonDto : categorySeedJsonDtos) {
            if (!this.validationUtil.isValid(categorySeedJsonDto)) {
                System.out.println(this.validationUtil.validate(categorySeedJsonDto)
                        .stream()
                        .map(ConstraintViolation::getMessage)
                        .collect(Collectors.joining(System.lineSeparator())));
                continue;
            }

            this.categoryRepository.save(this.modelMapper.map(categorySeedJsonDto, Category.class));
        }
    }

    @Override
    public boolean isImported() {
        return this.categoryRepository.count() > 0;
    }

    @Override
    public void exportCategories() throws IOException {
        Set<CategoryExportsonDto> collected = this.categoryRepository.findAll()
                .stream().map(c -> {
                    double sum = c.getProducts().stream().mapToDouble(p -> p.getPrice().doubleValue()).sum();
                    CategoryExportsonDto categoryExportsonDto =
                            new CategoryExportsonDto(c.getName(),
                                    c.getProducts().size(),
                                    BigDecimal.valueOf(sum / c.getProducts().size()),
                                    BigDecimal.valueOf(sum));
                    return categoryExportsonDto;
                })
                .sorted((a, b) -> Integer.compare(b.getProductsCount(), a.getProductsCount()))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        String json = this.gson.toJson(collected);

        FileWriter writer = new FileWriter("src/main/resources/json/exports/categories-by-products.json");
        writer.write(json);
        writer.close();
    }
}
