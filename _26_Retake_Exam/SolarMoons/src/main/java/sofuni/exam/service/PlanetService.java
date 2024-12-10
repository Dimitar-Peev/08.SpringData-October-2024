package sofuni.exam.service;

import java.io.IOException;

public interface PlanetService {

    boolean areImported();

    String readPlanetsFileContent() throws IOException;

    String importPlanets() throws IOException;
}
