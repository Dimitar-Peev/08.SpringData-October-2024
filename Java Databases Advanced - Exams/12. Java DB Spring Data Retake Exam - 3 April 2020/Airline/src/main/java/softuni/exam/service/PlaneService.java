package softuni.exam.service;

import jakarta.xml.bind.JAXBException;

import java.io.IOException;

public interface PlaneService {

    boolean areImported();

    String readPlanesFileContent() throws IOException;

    String importPlanes() throws IOException, JAXBException;
}
