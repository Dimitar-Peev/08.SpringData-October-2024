package org.bookshop._12_exercise_spring_data_introduction.service;

import java.io.IOException;

public interface AuthorService {

    void seedAuthors() throws IOException;

    boolean areAuthorsImported();

    void printAllAuthorsWithOneBookBefore1990();

    void printAllAuthorsWithDescCount();
}
