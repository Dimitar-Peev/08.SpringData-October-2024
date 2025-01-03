package softuni.exam.instagraphlite.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.instagraphlite.models.dtos.xml.PostSeedDto;
import softuni.exam.instagraphlite.models.dtos.xml.PostSeedRootDto;
import softuni.exam.instagraphlite.models.entities.Post;
import softuni.exam.instagraphlite.repository.PostRepository;
import softuni.exam.instagraphlite.service.PictureService;
import softuni.exam.instagraphlite.service.PostService;
import softuni.exam.instagraphlite.service.UserService;
import softuni.exam.instagraphlite.util.ValidationUtil;
import softuni.exam.instagraphlite.util.XmlParser;

import jakarta.xml.bind.JAXBException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class PostServiceImpl implements PostService {

    private static final String FILE_PATH = "src/main/resources/files/posts.xml";

    private final PostRepository postRepository;
    private final UserService userService;
    private final PictureService pictureService;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;
    private final XmlParser xmlParser;

    public PostServiceImpl(PostRepository postRepository, UserService userService, PictureService pictureService,
                           ValidationUtil validationUtil, ModelMapper modelMapper, XmlParser xmlParser) {
        this.postRepository = postRepository;
        this.userService = userService;
        this.pictureService = pictureService;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.xmlParser = xmlParser;
    }

    @Override
    public boolean areImported() {
        return this.postRepository.count() > 0;
    }

    @Override
    public String readFromFileContent() throws IOException {
        return Files.readString(Path.of(FILE_PATH));
    }

    @Override
    public String importPosts() throws IOException, JAXBException {
        StringBuilder output = new StringBuilder();

        PostSeedRootDto postSeedRootDto = this.xmlParser.fromFile(FILE_PATH, PostSeedRootDto.class);

        postSeedRootDto.getPosts()
                .stream()
                .filter(postSeedDto -> {
                    boolean isValid = this.validationUtil.isValid(postSeedDto)
                            && this.userService.isExist(postSeedDto.getUser().getUsername())
                            && this.pictureService.isExist(postSeedDto.getPicture().getPath());

                    output.append(isValid
                                    ? "Successfully imported Post, made by " + postSeedDto.getUser().getUsername()
                                    : "Invalid Post")
                            .append(System.lineSeparator());

                    return isValid;
                })
                .map(this::newPost)
                .forEach(postRepository::save);

        return output.toString().trim();
    }

    private Post newPost(PostSeedDto postSeedDto) {
        Post post = this.modelMapper.map(postSeedDto, Post.class);
        post.setUser(this.userService.findByUsername(postSeedDto.getUser().getUsername()));
        post.setPicture(this.pictureService.findByPath(postSeedDto.getPicture().getPath()));
        return post;
    }
}
