package org.example.GameStore.service.impl;

import jakarta.validation.ConstraintViolation;
import org.example.GameStore.data.entities.Game;
import org.example.GameStore.data.entities.User;
import org.example.GameStore.data.repositories.GameRepository;
import org.example.GameStore.data.repositories.UserRepository;
import org.example.GameStore.service.GameService;
import org.example.GameStore.service.UserService;
import org.example.GameStore.service.dtos.*;
import org.example.GameStore.utils.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GameServiceImpl implements GameService {
    private final GameRepository gameRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final UserService userService;

    private final Set<Game> games = new HashSet<>();

    public GameServiceImpl(GameRepository gameRepository, UserRepository userRepository, ModelMapper modelMapper, ValidationUtil validationUtil, UserService userService) {
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.userService = userService;
    }

    @Override
    public String addGame(GameCreateDto gameCreateDTO) {
        if (!this.userService.isAdmin()) {
            return "You do not have permission to add this game";
        }

        if (!this.validationUtil.isValid(gameCreateDTO)) {
            return this.validationUtil.validate(gameCreateDTO)
                    .stream().map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(System.lineSeparator()));
        }

        Game game = this.modelMapper.map(gameCreateDTO, Game.class);
        this.gameRepository.saveAndFlush(game);
        return String.format("Added %s", game.getTitle());
    }

    @Override
    public String editGame(GameEditDto gameEditDto) {
        if (!this.userService.isAdmin()) {
            return "You do not have permission to edit this game";
        }

        if (!this.validationUtil.isValid(gameEditDto)) {
            return this.validationUtil.validate(gameEditDto)
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(System.lineSeparator()));
        }

        Optional<Game> optionalGame = this.gameRepository.findById(gameEditDto.getId());
        if (optionalGame.isEmpty()) {
            return "Not such game found";
        }

        Game game = optionalGame.get();

        if (gameEditDto.getPrice() != null) {
            game.setPrice(gameEditDto.getPrice());
        }

        if (gameEditDto.getSize() != null) {
            game.setSize(gameEditDto.getSize());
        }

        this.gameRepository.saveAndFlush(game);
        return String.format("Edited %s", game.getTitle());
    }

    @Override
    public String deleteGame(int id) {
        if (!this.userService.isAdmin()) {
            return "You do not have permission to delete this game";
        }

        Optional<Game> gameById = this.gameRepository.findById(id);
        if (gameById.isEmpty()) {
            return "Not such game found";
        }

        this.gameRepository.delete(gameById.get());
        return String.format("Deleted %s", gameById.get().getTitle());
    }

    @Override
    public Set<GameViewDto> getAllGames() {
        return this.gameRepository.findAll()
                .stream()
                .map(g -> this.modelMapper.map(g, GameViewDto.class)).collect(Collectors.toSet());
    }

    @Override
    public GameDetailDto getDetailGame(String title) {
        Optional<Game> gameByTitle = this.gameRepository.findByTitle(title);

        return gameByTitle.map(game -> this.modelMapper.map(game, GameDetailDto.class)).orElse(new GameDetailDto());
    }

    @Override
    public String getOwnedGames() {
        if (!this.userService.isLoggedIn()) {
            return "Not logged in user";
        }

        return this.userService.getUser()
                .getGames()
                .stream()
                .map(g -> this.modelMapper.map(g, GameOwnedDto.class))
                .map(GameOwnedDto::getTitle).collect(Collectors.joining(System.lineSeparator()));
    }

    @Override
    public String addItem(String title) {
        if (!this.userService.isLoggedIn()) {
            return "No logged in user";
        }

        Optional<Game> gameByTitle = this.gameRepository.findByTitle(title);
        if (gameByTitle.isEmpty()) {
            return "Not such game found";
        }

        Game game = gameByTitle.get();
        this.games.add(game);
        return String.format("%s added to cart.", game.getTitle());
    }

    @Override
    public String removeItem(String title) {
        if (!this.userService.isLoggedIn()) {
            return "No logged in user";
        }

        Optional<Game> gameByTitle = this.gameRepository.findByTitle(title);
        if (gameByTitle.isEmpty()) {
            return "Not such game found";
        }

        if (this.games.contains(gameByTitle.get())) {
            this.games.remove(gameByTitle.get());
            return String.format("%s removed from cart.", gameByTitle.get().getTitle());
        }

        return "Not such game found in cart";
    }

    @Override
    public String buyItems() {

        if (!this.userService.isLoggedIn()) {
            return "No logged in user";
        }

        User user = this.userService.getUser();
        Set<Game> newGames = games.stream().filter(g -> !user.getGames().contains(g)).collect(Collectors.toSet());
        user.getGames().addAll(newGames);
        this.userRepository.saveAndFlush(user);

        if (!newGames.isEmpty()) {
            return "Successfully bought games:\n" +
                    newGames.stream().map(g -> " -" + g.getTitle())
                            .collect(Collectors.joining(System.lineSeparator()));
        }

        return "No bought games found.";
    }
}
