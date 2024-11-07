package org.example.GameStore.controller;

import org.example.GameStore.service.dtos.*;
import org.example.GameStore.service.GameService;
import org.example.GameStore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.stream.Collectors;

@Component
public class CommandLineRunnerImpl implements CommandLineRunner {
    private final UserService userService;
    private final GameService gameService;

    @Autowired
    public CommandLineRunnerImpl(UserService userService, GameService gameService) {
        this.userService = userService;
        this.gameService = gameService;
    }

    @Override
    public void run(String... args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Enter command:");
        while (true) {
            String[] inputArr = br.readLine().split("\\|");

            String output = "";
            switch (inputArr[0]) {
                case "RegisterUser":
                    output = this.userService.registerUser(new UserCreateDto(inputArr[1], inputArr[2], inputArr[3], inputArr[4]));
                    break;
                case "LoginUser":
                    output = this.userService.loginUser(new UserLoginDto(inputArr[1], inputArr[2]));
                    break;
                case "Logout":
                    output = this.userService.logout();
                    break;
                case "AddGame":
                    String title = inputArr[1];
                    BigDecimal price = new BigDecimal(inputArr[2]);
                    double size = Double.parseDouble(inputArr[3]);
                    String trailer = inputArr[4];
                    String thumbnailURL = inputArr[5];
                    String description = inputArr[6];
                    LocalDate releaseDate = LocalDate.parse(inputArr[7], DateTimeFormatter.ofPattern("dd-MM-yyyy"));

                    output = this.gameService.addGame(new GameCreateDto(title, price, size, trailer, thumbnailURL, description, releaseDate));
                    break;
                case "EditGame":
                    GameEditDto gameEditDto = new GameEditDto();
                    gameEditDto.setId(Integer.parseInt(inputArr[1]));
                    Arrays.stream(inputArr).skip(2).forEach(p -> {
                        String[] split = p.split("=");
                        String field = split[0];
                        if ("price".equals(field)) {
                            gameEditDto.setPrice(new BigDecimal(split[1]));
                        } else if ("size".equals(field)) {
                            gameEditDto.setSize(Double.parseDouble(split[1]));
                        }
                    });
                    output = this.gameService.editGame(gameEditDto);
                    break;
                case "DeleteGame":
                    output = this.gameService.deleteGame(Integer.parseInt(inputArr[1]));
                    break;
                case "AllGames":
                    output = this.gameService.getAllGames().stream().map(GameViewDto::toString).collect(Collectors.joining(System.lineSeparator()));
                    break;
                case "DetailGame":
                     output = this.gameService.getDetailGame(inputArr[1]).toString();
                    break;
                case "OwnedGames":
                    output = this.gameService.getOwnedGames();
                    break;
                case "AddItem":
                    output = this.gameService.addItem(inputArr[1]);
                    break;
                case "RemoveItem":
                    output = this.gameService.removeItem(inputArr[1]);
                    break;
                case "BuyItem":
                    output = this.gameService.buyItems();
                    break;
                default:
                    System.out.println("GoodBye!");
                    return;
            }

            System.out.println(output);
        }

    }
}
