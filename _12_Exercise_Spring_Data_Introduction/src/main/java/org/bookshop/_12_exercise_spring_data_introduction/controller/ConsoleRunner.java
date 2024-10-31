package org.bookshop._12_exercise_spring_data_introduction.controller;

import org.bookshop._12_exercise_spring_data_introduction.service.AuthorService;
import org.bookshop._12_exercise_spring_data_introduction.service.BookService;
import org.bookshop._12_exercise_spring_data_introduction.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ConsoleRunner implements CommandLineRunner {

    private final CategoryService categoryService;
    private final AuthorService authorService;
    private final BookService bookService;

    @Autowired
    public ConsoleRunner(CategoryService categoryService, AuthorService authorService, BookService bookService) {
        this.categoryService = categoryService;
        this.authorService = authorService;
        this.bookService = bookService;
    }

    @Override
    public void run(String... args) throws Exception {
//        System.out.println("Hello World!");
        seedData();

        // 1. Get all books after the year 2000. Print only their titles.
        this.bookService.printAllBooksAfter2000();
        System.out.println("**************************************************");
        // 2. Get all authors with at least one book with a release date before 1990. Print their first name and last name.
        this.authorService.printAllAuthorsWithOneBookBefore1990();
        System.out.println("**************************************************");
        // 3. Get all authors, ordered by the number of their books (descending). Print their first name, last name and book count.
        this.authorService.printAllAuthorsWithDescCount();
        System.out.println("**************************************************");
        // 4. Get all books from author George Powell, ordered by their release date (descending), then by book title (ascending). Print the book's title, release date and copies.
        this.bookService.printAllBooksFromGeorge();

//        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
//        Set<ConstraintViolation<Author>> constraintViolationSet = validator.validate(new Author("Dimitar", "Peev"));
//        constraintViolationSet.forEach(v -> v.getMessage());
    }

    private void seedData() throws IOException {
        if (!this.categoryService.isImported()) {
            this.categoryService.seedCategories();
        }
        if (!this.authorService.areAuthorsImported()) {
            this.authorService.seedAuthors();
        }
        if (!this.bookService.areBooksImported()) {
            this.bookService.seedBooks();
        }
    }
}
