package com.example.nltworkshop.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public abstract class BaseServiceImpl implements BaseService {

    protected String readFile(String path) throws IOException {
        return Files.readString(Path.of(path));
    }
}
