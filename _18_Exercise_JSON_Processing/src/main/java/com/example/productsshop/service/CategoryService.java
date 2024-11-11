package com.example.productsshop.service;

import java.io.IOException;

public interface CategoryService {
    void seedCategories() throws IOException;

    boolean isImported();

    void exportCategories() throws IOException;
}
