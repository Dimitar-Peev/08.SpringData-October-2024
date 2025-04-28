package softuni.exam.service;

import jakarta.xml.bind.JAXBException;
import softuni.exam.models.entity.Seller;

import java.io.IOException;

public interface SellerService {
    
    boolean areImported();

    String readSellersFromFile() throws IOException;

    String importSellers() throws IOException, JAXBException;

    Seller findById(long id);
}
