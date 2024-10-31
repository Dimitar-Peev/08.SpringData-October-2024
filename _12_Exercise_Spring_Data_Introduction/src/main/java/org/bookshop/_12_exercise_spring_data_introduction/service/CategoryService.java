package org.bookshop._12_exercise_spring_data_introduction.service;

import java.io.IOException;

public interface CategoryService {
    void seedCategories() throws IOException;

    boolean isImported();
}
