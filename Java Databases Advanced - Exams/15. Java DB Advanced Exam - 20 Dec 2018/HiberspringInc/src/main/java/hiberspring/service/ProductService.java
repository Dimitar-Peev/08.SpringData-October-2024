package hiberspring.service;

import jakarta.xml.bind.JAXBException;

import java.io.IOException;

public interface ProductService {

    Boolean productsAreImported();

    String readProductsXmlFile() throws IOException;

    String importProducts() throws JAXBException, IOException;
}
