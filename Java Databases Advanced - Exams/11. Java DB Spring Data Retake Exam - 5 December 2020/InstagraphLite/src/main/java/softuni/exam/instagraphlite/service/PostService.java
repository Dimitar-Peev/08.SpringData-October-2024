package softuni.exam.instagraphlite.service;

import jakarta.xml.bind.JAXBException;

import java.io.IOException;

public interface PostService {

    boolean areImported();

    String readFromFileContent() throws IOException;

    String importPosts() throws IOException, JAXBException;
}
