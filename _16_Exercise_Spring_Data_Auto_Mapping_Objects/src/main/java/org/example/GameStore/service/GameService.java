package org.example.GameStore.service;

import org.example.GameStore.service.dtos.*;

import java.util.Set;

public interface GameService {
    String addGame(GameCreateDto gameCreateDTO);

    String editGame(GameEditDto gameEditDto);

    String deleteGame(int id);

    Set<GameViewDto> getAllGames();

    GameDetailDto getDetailGame(String title);

    String getOwnedGames();

    String addItem(String title);

    String removeItem(String title);

    String buyItems();
}
