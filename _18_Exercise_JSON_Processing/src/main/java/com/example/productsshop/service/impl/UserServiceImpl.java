package com.example.productsshop.service.impl;

import com.example.productsshop.data.entities.User;
import com.example.productsshop.data.repositories.UserRepository;
import com.example.productsshop.service.UserService;
import com.example.productsshop.service.dtos.UserSeedJsonDto;
import com.example.productsshop.service.dtos.export.*;
import com.example.productsshop.utils.ValidationUtil;
import com.google.gson.Gson;
import jakarta.validation.ConstraintViolation;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private static final String JSON_PATH = "src/main/resources/json/users.json";

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final ValidationUtil validationUtil;

    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, Gson gson, ValidationUtil validationUtil) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.validationUtil = validationUtil;
    }

    @Override
    public void seedUsers() throws IOException {

        String jsonString = String.join("", Files.readAllLines(Path.of(JSON_PATH)));

        UserSeedJsonDto[] userSeedJsonDtos = this.gson.fromJson(jsonString, UserSeedJsonDto[].class);

        for (UserSeedJsonDto userSeedJsonDto : userSeedJsonDtos) {
            if (!this.validationUtil.isValid(userSeedJsonDto)) {
                System.out.println(this.validationUtil.validate(userSeedJsonDto)
                        .stream()
                        .map(ConstraintViolation::getMessage)
                        .collect(Collectors.joining(System.lineSeparator())));
                continue;
            }

            this.userRepository.save(this.modelMapper.map(userSeedJsonDto, User.class));
        }

    }

    @Override
    public boolean isImported() {
        return this.userRepository.count() > 0;
    }

    @Override
    public void successfullySoldProducts() throws IOException {
        Set<User> allBySoldIsNotNull = this.userRepository.findAllBySoldIsNotNull();

        Set<UserSoldJsonDto> collected = allBySoldIsNotNull
                .stream()
                .map(user -> {
                    UserSoldJsonDto userSoldJsonDto = this.modelMapper.map(user, UserSoldJsonDto.class);
                    userSoldJsonDto.setSoldProducts(user.getSold().stream()
                            .map(p -> this.modelMapper.map(p, ProductSoldJsonDto.class)).collect(Collectors.toSet()));

                    return userSoldJsonDto;
                }).collect(Collectors.toSet());


        String json = this.gson.toJson(collected);

        FileWriter writer = new FileWriter("src/main/resources/json/exports/successfully-sold-products.json");
        writer.write(json);
        writer.close();
    }

    @Override
    public void usersAndProducts() throws IOException {
        UserAndSoldProductDto userAndSoldProductDto = new UserAndSoldProductDto();
        Set<User> query = this.userRepository.findAllBySoldIsNotNullOrderBySoldDesc();

        Set<FullUserSoldJsonDto> collected = query.stream().map(u -> {
            FullUserSoldJsonDto fullUserSoldJsonDto = this.modelMapper.map(u, FullUserSoldJsonDto.class);
            SoldProductsDto soldProductsDto = new SoldProductsDto();
            soldProductsDto.setCount(u.getSold().size());
            soldProductsDto.setProducts(
                    u.getSold().stream()
                            .map(p -> this.modelMapper.map(p, ProductInfoDto.class))
                            .collect(Collectors.toSet()));
            fullUserSoldJsonDto.setSoldProducts(soldProductsDto);
            return fullUserSoldJsonDto;
        }).collect(Collectors.toSet());

        userAndSoldProductDto.setUsersCount(query.size());
        userAndSoldProductDto.setUsers(collected);

        String json = this.gson.toJson(userAndSoldProductDto);

        FileWriter writer = new FileWriter("src/main/resources/json/exports/users-and-products.json");
        writer.write(json);
        writer.close();
    }
}
