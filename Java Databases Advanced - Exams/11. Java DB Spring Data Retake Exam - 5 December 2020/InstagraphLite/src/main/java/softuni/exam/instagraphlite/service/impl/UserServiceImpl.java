package softuni.exam.instagraphlite.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.instagraphlite.models.dtos.json.UserSeedDto;
import softuni.exam.instagraphlite.models.entities.User;
import softuni.exam.instagraphlite.repository.UserRepository;
import softuni.exam.instagraphlite.service.PictureService;
import softuni.exam.instagraphlite.service.UserService;
import softuni.exam.instagraphlite.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;

@Service
public class UserServiceImpl implements UserService {

    private static final Locale locale = Locale.US;
    private static final DecimalFormat df = new DecimalFormat("####.00", DecimalFormatSymbols.getInstance(locale));

    private static final String FILE_PATH = "src/main/resources/files/users.json";

    private final UserRepository userRepository;
    private final PictureService pictureService;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final Gson gson;

    public UserServiceImpl(UserRepository userRepository, PictureService pictureService, ModelMapper modelMapper,
                           ValidationUtil validationUtil, Gson gson) {
        this.userRepository = userRepository;
        this.pictureService = pictureService;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gson = gson;
    }

    @Override
    public boolean areImported() {
        return this.userRepository.count() > 0;
    }

    @Override
    public String readFromFileContent() throws IOException {
        return Files.readString(Path.of(FILE_PATH));
    }

    @Override
    public String importUsers() throws IOException {
        StringBuilder output = new StringBuilder();

        UserSeedDto[] userSeedDtos = this.gson.fromJson(readFromFileContent(), UserSeedDto[].class);

        Arrays.stream(userSeedDtos)
                .filter(userSeedDto -> {

                    boolean isValid = this.validationUtil.isValid(userSeedDto)
                            && !isExist(userSeedDto.getUsername())
                            && this.pictureService.isExist(userSeedDto.getProfilePicture());

                    output.append(isValid
                                    ? "Successfully imported User: " + userSeedDto.getUsername()
                                    : "Invalid User")
                            .append(System.lineSeparator());

                    return isValid;
                })
                .map(this::newUser)
                .forEach(userRepository::save);

        return output.toString().trim();
    }

    private User newUser(UserSeedDto userSeedDto) {
        User user = this.modelMapper.map(userSeedDto, User.class);
        user.setProfilePicture(this.pictureService.findByPath(userSeedDto.getProfilePicture()));
        return user;
    }

    @Override
    public boolean isExist(String username) {
        return this.userRepository.existsByUsername(username);
    }

    @Override
    public String exportUsersWithTheirPosts() {
        StringBuilder output = new StringBuilder();

        this.userRepository
                .findAllByPostsCountDescThenByUserId()
                .forEach(user -> {
                    output.append(String.format("""
                            User: %s
                            Post count: %d
                            """, user.getUsername(), user.getPosts().size()));

                    user.getPosts()
                            .stream()
                            .sorted(Comparator.comparingDouble(a -> a.getPicture().getSize()))
                            .forEach(post -> {
                                output.append(String.format("""
                                                ==Post Details:
                                                ----Caption: %s
                                                ----Picture Size: %s
                                                """,
                                        post.getCaption(),
                                        df.format(post.getPicture().getSize())));
                            });
                });

        return output.toString().trim();
    }

    @Override
    public User findByUsername(String username) {
        return this.userRepository.findByUsername(username).orElse(null);
    }
}
