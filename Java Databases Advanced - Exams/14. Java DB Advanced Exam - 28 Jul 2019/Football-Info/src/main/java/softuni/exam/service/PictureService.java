package softuni.exam.service;

import jakarta.xml.bind.JAXBException;
import softuni.exam.domain.entities.Picture;

import java.io.IOException;

public interface PictureService {

    String importPictures() throws IOException, JAXBException;

    boolean areImported();

    String readPicturesXmlFile() throws IOException;

    Picture getPictureByUrl(String url);
}
