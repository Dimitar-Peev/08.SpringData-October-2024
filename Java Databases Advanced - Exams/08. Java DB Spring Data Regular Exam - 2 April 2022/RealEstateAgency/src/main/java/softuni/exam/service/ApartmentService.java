package softuni.exam.service;

import jakarta.xml.bind.JAXBException;
import java.io.IOException;

public interface ApartmentService {
    
    boolean areImported();

    String readApartmentsFromFile() throws IOException;

    String importApartments() throws IOException, JAXBException;
}
