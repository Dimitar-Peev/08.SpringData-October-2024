package exam.service;

import exam.model.entities.Town;
import jakarta.xml.bind.JAXBException;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface TownService {

    boolean areImported();

    String readTownsFileContent() throws IOException;

    String importTowns() throws FileNotFoundException, JAXBException;

    Town findTownByName(String townName);
}
