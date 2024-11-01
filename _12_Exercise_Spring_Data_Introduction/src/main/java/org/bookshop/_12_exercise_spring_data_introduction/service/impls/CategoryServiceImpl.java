package org.bookshop._12_exercise_spring_data_introduction.service.impls;

import org.bookshop._12_exercise_spring_data_introduction.data.entities.Category;
import org.bookshop._12_exercise_spring_data_introduction.data.repositories.CategoryRepository;
import org.bookshop._12_exercise_spring_data_introduction.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

@Service
public class CategoryServiceImpl implements CategoryService {
    private static final String CATEGORIES_PATH = "src/main/resources/files/categories.txt";

    private final CategoryRepository categoryRepository;

//    @Autowired
//    private ApplicationContext context;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void seedCategories() throws IOException {
        // read categories.txt
//        CategoryRepository categoryRepository1 = context.getBean("CategoryRepository", CategoryRepository.class);
        Set<Category> categories = new HashSet<>();

        Files.readAllLines(Path.of(CATEGORIES_PATH))
                .stream()
                .filter(line -> !line.trim().isEmpty())
                .forEach(line -> {
                    Category category = new Category(line);
                    categories.add(category);
                });

        this.categoryRepository.saveAllAndFlush(categories);
        System.out.printf("Count of imported Categories - %d%n", this.categoryRepository.count());
    }

    @Override
    public boolean isImported() {
        return this.categoryRepository.count() > 0;
    }
}
