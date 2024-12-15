package exam.service;

import jakarta.xml.bind.JAXBException;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface ShopService {

    boolean areImported();

    String readShopsFileContent() throws IOException;

    String importShops() throws FileNotFoundException, JAXBException;
}
