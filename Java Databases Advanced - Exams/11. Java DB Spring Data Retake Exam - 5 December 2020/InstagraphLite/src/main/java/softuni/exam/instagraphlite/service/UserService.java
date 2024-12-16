package softuni.exam.instagraphlite.service;

import softuni.exam.instagraphlite.models.entities.User;

import java.io.IOException;

public interface UserService {

    boolean areImported();

    String readFromFileContent() throws IOException;

    String importUsers() throws IOException;

    boolean isExist(String path);

    String exportUsersWithTheirPosts();

    User findByUsername(String username);
}
