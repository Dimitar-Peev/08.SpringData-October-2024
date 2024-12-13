package com.example.football.service.impl;

import com.example.football.models.dto.xml.PlayerSeedDto;
import com.example.football.models.dto.xml.PlayerSeedRootDto;
import com.example.football.models.entity.Player;
import com.example.football.repository.PlayerRepository;
import com.example.football.repository.StatRepository;
import com.example.football.repository.TeamRepository;
import com.example.football.repository.TownRepository;
import com.example.football.service.PlayerService;
import com.example.football.util.ValidationUtil;
import com.example.football.util.XmlParser;
import jakarta.xml.bind.JAXBException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

@Service
public class PlayerServiceImpl implements PlayerService {

    private static final String FILE_PATH = "src/main/resources/files/xml/players.xml";

    private final PlayerRepository playerRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final XmlParser xmlParser;
    private final TeamRepository teamRepository;
    private final TownRepository townRepository;
    private final StatRepository statRepository;

    public PlayerServiceImpl(PlayerRepository playerRepository, ModelMapper modelMapper, ValidationUtil validationUtil,
                             XmlParser xmlParser, TeamRepository teamRepository, TownRepository townRepository, StatRepository statRepository) {
        this.playerRepository = playerRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.xmlParser = xmlParser;
        this.teamRepository = teamRepository;
        this.townRepository = townRepository;
        this.statRepository = statRepository;
    }

    @Override
    public boolean areImported() {
        return this.playerRepository.count() > 0;
    }

    @Override
    public String readPlayersFileContent() throws IOException {
        return Files.readString(Path.of(FILE_PATH));
    }

    @Override
    public String importPlayers() throws JAXBException, IOException {
        StringBuilder output = new StringBuilder();

        PlayerSeedRootDto playerSeedRootDto = this.xmlParser.convertFromFile(FILE_PATH, PlayerSeedRootDto.class);

        playerSeedRootDto.getPlayers()
                .stream()
                .filter(playerSeedDto -> {
                    boolean isExist = this.playerRepository.existsByEmail(playerSeedDto.getEmail());

                    boolean isValid = this.validationUtil.isValid(playerSeedDto) && !isExist;

                    output.append(isValid ? String.format("Successfully imported Player %s %s - %s",
                                    playerSeedDto.getFirstName(),
                                    playerSeedDto.getLastName(),
                                    playerSeedDto.getPosition())
                                    : "Invalid Player")
                            .append(System.lineSeparator());
                    return isValid;
                })
                .map(this::newPlayer)
                .forEach(playerRepository::save);

        return output.toString().trim();
    }

    @Override
    public String exportBestPlayers() {
        LocalDate after = LocalDate.of(1995, 1, 1);
        LocalDate before = LocalDate.of(2003, 1, 1);

        List<Player> bestPlayers = this.playerRepository
                .findAllByBirthDateAfterAndBirthDateBeforeOrderByStatShootingDescStatPassingDescStatEnduranceDescLastNameAsc(after, before);

        StringBuilder output = new StringBuilder();
        bestPlayers.forEach(player -> output.append(player.toString()));
        return output.toString();
    }

    private Player newPlayer(PlayerSeedDto playerSeedDto) {
        Player player = this.modelMapper.map(playerSeedDto, Player.class);

        player.setTeam(this.teamRepository.findByName(playerSeedDto.getTeam().getName()));
        player.setTown(this.townRepository.findByName(playerSeedDto.getTown().getName()));
        player.setStat(this.statRepository.findById(playerSeedDto.getStat().getId()).orElse(null));

        return player;
    }
}
