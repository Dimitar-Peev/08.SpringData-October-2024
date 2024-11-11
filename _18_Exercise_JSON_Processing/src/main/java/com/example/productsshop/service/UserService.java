package com.example.productsshop.service;

import java.io.IOException;

public interface UserService {
    void seedUsers() throws IOException;

    boolean isImported();

    void successfullySoldProducts() throws IOException;

    void usersAndProducts() throws IOException;
}
