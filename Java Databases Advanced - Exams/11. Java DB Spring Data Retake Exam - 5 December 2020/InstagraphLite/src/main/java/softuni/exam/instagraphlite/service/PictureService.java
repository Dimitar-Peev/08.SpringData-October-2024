package softuni.exam.instagraphlite.service;

import softuni.exam.instagraphlite.models.entities.Picture;

import java.io.IOException;

public interface PictureService {

    boolean areImported();

    String readFromFileContent() throws IOException;

    String importPictures() throws IOException;

    boolean isExist(String path);

    String exportPictures();

    Picture findByPath(String profilePicture);
}
