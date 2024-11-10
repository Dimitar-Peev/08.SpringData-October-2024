package com.example.productsshop.controller;

import com.example.productsshop.service.CategoryService;
import com.example.productsshop.service.ProductService;
import com.example.productsshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Scanner;

@Component
public class CommandLineRunnerImpl implements CommandLineRunner {

    private final CategoryService categoryService;
    private final ProductService productService;
    private final UserService userService;

    @Autowired
    public CommandLineRunnerImpl(CategoryService categoryService, ProductService productService, UserService userService) {
        this.categoryService = categoryService;
        this.productService = productService;
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {
        seedData();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter exercise number: ");
        int exerciseNumber = Integer.parseInt(scanner.nextLine());

        switch (exerciseNumber) {
            case 1 -> this.productService.exportProductsInRange();
            case 2 -> this.userService.successfullySoldProducts();
            case 3 -> this.categoryService.exportCategories();
            case 4 -> this.userService.usersAndProducts();
        }

    }

    private void seedData() throws IOException {
        if (!this.categoryService.isImported()) {
            this.categoryService.seedCategories();
        }

        if (!this.userService.isImported()) {
            this.userService.seedUsers();
        }

        if (!this.productService.isImported()) {
            this.productService.seedProducts();
        }
    }
}
