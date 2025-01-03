package softuni.exam.service;

import softuni.exam.domain.entities.Team;

import jakarta.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;

public interface TeamService {

    String importTeams() throws JAXBException, FileNotFoundException;

    boolean areImported();

    String readTeamsXmlFile() throws IOException;

    Team getTeamByName(String name);
}
