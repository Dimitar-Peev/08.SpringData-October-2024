package org.example._13_spring_data_advanced_querying.controller;

import org.example._13_spring_data_advanced_querying.entities.Ingredient;
import org.example._13_spring_data_advanced_querying.entities.Label;
import org.example._13_spring_data_advanced_querying.entities.Shampoo;
import org.example._13_spring_data_advanced_querying.entities.enums.Size;
import org.example._13_spring_data_advanced_querying.repositories.CustomRepo;
import org.example._13_spring_data_advanced_querying.repositories.IngredientRepository;
import org.example._13_spring_data_advanced_querying.repositories.LabelRepository;
import org.example._13_spring_data_advanced_querying.repositories.ShampooRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class ConsoleRunner implements CommandLineRunner {

    private final ShampooRepository shampooRepository;
    private final LabelRepository labelRepository;
    private final IngredientRepository ingredientRepository;
    private final CustomRepo customRepo;

    public ConsoleRunner(ShampooRepository shampooRepository,
                         LabelRepository labelRepository,
                         IngredientRepository ingredientRepository,
                         CustomRepo customRepo) {
        this.shampooRepository = shampooRepository;
        this.labelRepository = labelRepository;
        this.ingredientRepository = ingredientRepository;
        this.customRepo = customRepo;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Enter exercise number(1 - 11):");
        Scanner scanner = new Scanner(System.in);
        int exerciseNumber = Integer.parseInt(scanner.nextLine());

        switch (exerciseNumber) {
            case 1 -> selectShampoosBySize(scanner);
            case 2 -> selectShampoosBySizeOrLabel(scanner);
            case 3 -> selectShampoosByPrice(scanner);
            case 4 -> selectIngredientsByName(scanner);
            case 5 -> selectIngredientsByNames(scanner);
            case 6 -> countShampoosByPrice(scanner);
            case 7 -> selectShampoosByIngredients(scanner); //
            case 8 -> selectShampoosByIngredientsCount(scanner);
            case 9 -> deleteIngredientsByName(scanner);
            case 10 -> updateIngredientsPriceBy10Percent();
            case 11 -> updateIngredientsPricesForNames(scanner);
            default -> System.out.println("You have entered an invalid number!");
        }

//        Optional<Shampoo> byId = shampooRepository.findById(1L);
//        System.out.println(byId.get().getId()); // 1

//        List<Shampoo> byBrand = shampooRepository.findByBrand("Swiss Green Apple & Nettle");
//        System.out.println(byBrand);

//        List<Shampoo> byBrand = shampooRepository.findByBrandAndSize("Swiss Green Apple & Nettle", Size.SMALL);
//        System.out.println(byBrand);

//        System.out.println(shampooRepository.findByLabelTitleContaining("strength"));

//        List<Shampoo> shampoos = shampooRepository.findByIngredientsCountLessThan(2);
//        System.out.println(shampoos);

//        List<Shampoo> byIngredients = shampooRepository.findByIngredientsNameInQuery(List.of("Berry", "Mineral-Collagen"));
//        byIngredients.forEach(shampoo -> System.out.println(shampoo.getBrand()));
        
//        Shampoo firstShampoo = customRepo.getFirstShampoo();
//        System.out.println(firstShampoo);

//        Optional<Shampoo> shampoo = shampooRepository.findById(1L);
//        // @Transactional if this is a LAZY collection
//        Set<Ingredient> ingredients = shampoo.get().getIngredients();
//        System.out.println(ingredients.size());

//        List<Shampoo> byBrandNative = shampooRepository.findByBrandNative("Swiss Green Apple & Nettle");

    }

    private void selectShampoosBySize(Scanner scanner) {
        System.out.println("Enter shampoo size:");
        String size = scanner.nextLine();
        List<Shampoo> byBrand = shampooRepository.findBySizeOrderByIdAsc(Size.valueOf(size.toUpperCase()));
        byBrand.forEach(shampoo -> {
            System.out.printf("%s %s %.2flv.%n", shampoo.getBrand(), shampoo.getSize().name(), shampoo.getPrice());
        });
    }

    private void selectShampoosBySizeOrLabel(Scanner scanner) {
        System.out.println("Enter shampoo size:");
        String sizeName = scanner.nextLine().toUpperCase();
        Size size = Size.valueOf(sizeName);
        System.out.println("Enter label id:");
        long labelId = Long.parseLong(scanner.nextLine());
        Optional<Label> labelById = labelRepository.findById(labelId);
        List<Shampoo> bySizeOrLabel = shampooRepository.findBySizeOrLabelOrderByPriceAsc(size, labelById.get());
        bySizeOrLabel.forEach(shampoo -> {
            System.out.printf("%s %s %.2flv.%n", shampoo.getBrand(), shampoo.getSize().name(), shampoo.getPrice());
        });
    }

    private void selectShampoosByPrice(Scanner scanner) {
        System.out.println("Enter price:");
        double price = Double.parseDouble(scanner.nextLine());
        List<Shampoo> byPrice = shampooRepository.findByPriceGreaterThanOrderByPriceDesc(new BigDecimal(price));
        byPrice.forEach(shampoo -> {
            System.out.printf("%s %s %.2flv.%n", shampoo.getBrand(), shampoo.getSize().name(), shampoo.getPrice());
        });
    }

    private void selectIngredientsByName(Scanner scanner) {
        System.out.println("Enter letters:");
        String letters = scanner.nextLine();
        List<Ingredient> ingredients = ingredientRepository.findByNameStartingWith(letters);
        ingredients.forEach(ingredient -> System.out.println(ingredient.getName()));
    }

    private void selectIngredientsByNames(Scanner scanner) {
        System.out.println("Enter ingredients separated by a space:");
        List<String> names = Arrays.stream(scanner.nextLine().split("\\s+")).collect(Collectors.toList());
        List<Ingredient> ingredients = ingredientRepository.findByNameInOrderByPriceAsc(names);
        ingredients.forEach(ingredient -> System.out.println(ingredient.getName()));
    }

    private void countShampoosByPrice(Scanner scanner) {
        System.out.println("Enter price:");
        double price = Double.parseDouble(scanner.nextLine());
        int count = shampooRepository.countByPriceLessThan(BigDecimal.valueOf(price));
        System.out.println(count);
    }

    private void selectShampoosByIngredients(Scanner scanner) {
        System.out.println("Enter ingredients separated by a space:");
        List<String> ingredientNames = Arrays.stream(scanner.nextLine().split("\\s+")).collect(Collectors.toList());

//        Set<Shampoo> byIngredients = shampooRepository.findByIngredientsNameIn(ingredientNames);
        Set<Shampoo> byIngredients = shampooRepository.findByIngredientsNameInQuery(ingredientNames);

        byIngredients.forEach(shampoo -> System.out.println(shampoo.getBrand()));
    }

    private void selectShampoosByIngredientsCount(Scanner scanner) {
        System.out.println("Enter count:");
        int count = Integer.parseInt(scanner.nextLine());
        List<Shampoo> byIngredientsCount = shampooRepository.findByIngredientsCountLessThan(count);
        byIngredientsCount.forEach(shampoo -> System.out.println(shampoo.getBrand()));
    }

    public void deleteIngredientsByName(Scanner scanner) {
        System.out.println("Enter name:");
        String name = scanner.nextLine();
        ingredientRepository.deleteByName(name);
    }

    private void updateIngredientsPriceBy10Percent() {
        ingredientRepository.updateIngredientsPriceBy10Percent();
    }

    private void updateIngredientsPricesForNames(Scanner scanner) {
        System.out.println("Enter ingredients separated by a comma:");
        List<String> ingredientNames = Arrays.stream(scanner.nextLine().split(",")).collect(Collectors.toList());
        ingredientRepository.updateIngredientsPricesForNames(ingredientNames);
    }
}
