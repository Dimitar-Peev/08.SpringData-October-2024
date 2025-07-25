package softuni.exam.service;

import jakarta.xml.bind.JAXBException;
import java.io.IOException;

public interface ForecastService {

    boolean areImported();

    String readForecastsFromFile() throws IOException, JAXBException;
	
	String importForecasts() throws IOException, JAXBException;

    String exportForecasts();
}
