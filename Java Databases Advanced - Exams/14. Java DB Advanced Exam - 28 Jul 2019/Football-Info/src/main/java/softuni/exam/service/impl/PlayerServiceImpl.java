package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.domain.dtos.json.PlayerSeedDto;
import softuni.exam.domain.entities.Picture;
import softuni.exam.domain.entities.Player;
import softuni.exam.domain.entities.Team;
import softuni.exam.repository.PlayerRepository;
import softuni.exam.service.PictureService;
import softuni.exam.service.PlayerService;
import softuni.exam.service.TeamService;
import softuni.exam.util.ValidatorUtil;

import jakarta.transaction.Transactional;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static softuni.exam.constants.GlobalConstants.PLAYERS_FILE_PATH;

@Service
@Transactional
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;
    private final ModelMapper modelMapper;
    private final ValidatorUtil validatorUtil;
    private final Gson gson;
    private final TeamService teamService;
    private final PictureService pictureService;

    @Autowired
    public PlayerServiceImpl(PlayerRepository playerRepository, ModelMapper modelMapper, ValidatorUtil validatorUtil, Gson gson, TeamService teamService, PictureService pictureService) {
        this.playerRepository = playerRepository;
        this.modelMapper = modelMapper;
        this.validatorUtil = validatorUtil;
        this.gson = gson;
        this.teamService = teamService;
        this.pictureService = pictureService;
    }

    @Override
    public boolean areImported() {
        return this.playerRepository.count() > 0;
    }

    @Override
    public String readPlayersJsonFile() throws IOException {
        return Files.readString(Path.of(PLAYERS_FILE_PATH));
    }

    @Override
    public String importPlayers() throws IOException {
        StringBuilder sb = new StringBuilder();

        PlayerSeedDto[] playerSeedDtos = this.gson.fromJson(readPlayersJsonFile(), PlayerSeedDto[].class);

        Arrays.stream(playerSeedDtos)
                .forEach(playerSeedDto -> {
                    boolean isValid = this.validatorUtil.isValid(playerSeedDto);

                    if (isValid) {
                        Player byFirstNameAndLastName = this.playerRepository.findByFirstNameAndLastName(playerSeedDto.getFirstName(), playerSeedDto.getLastName());

                        if (byFirstNameAndLastName == null) {
                            Player player = this.modelMapper.map(playerSeedDto, Player.class);
                            Team teamByName = this.teamService.getTeamByName(playerSeedDto.getTeam().getName());
                            Picture pictureByUrl = this.pictureService.getPictureByUrl(playerSeedDto.getPicture().getUrl());

                            if (teamByName != null && pictureByUrl != null) {
                                player.setTeam(teamByName);
                                player.setPicture(pictureByUrl);
                                this.playerRepository.saveAndFlush(player);
                                sb.append(String.format("Successfully imported player: %s %s",
                                        playerSeedDto.getFirstName(), playerSeedDto.getLastName()));
                            } else {
                                sb.append("Invalid player");
                            }

                        } else {
                            sb.append("Already in DB");
                        }
                    } else {
                        sb.append("Invalid player");
                    }

                    sb.append(System.lineSeparator());
                });


        return sb.toString().trim();
    }

    @Override
    public String exportPlayersInATeam() {

        StringBuilder sb = new StringBuilder();

        List<Player> playersList = this.playerRepository.findAllByTeamName("North Hub");

        sb.append("Team: North Hub").append(System.lineSeparator());

        for (Player player : playersList) {
            sb.append(String.format("\t\tPlayer name: %s %s - %s\n" +
                            "\t\tNumber: %d\n",
                    player.getFirstName(), player.getLastName(), player.getPosition(), player.getNumber()));
        }

        return sb.toString().trim();
    }

    @Override
    public String exportPlayersWhereSalaryBiggerThan() {

        Locale locale = Locale.US;
        DecimalFormat df = new DecimalFormat("####.00", DecimalFormatSymbols.getInstance(locale));

        StringBuilder sb = new StringBuilder();

        List<Player> playersList = this.playerRepository.findAllBySalaryGreaterThanOrderBySalaryDesc(BigDecimal.valueOf(100000));

        for (Player player : playersList) {
            sb.append(String.format("Player name: %s %s \n" +
                            "\t\tNumber: %s\n" +
                            "\t\tSalary: %s\n" +
                            "\t\tTeam: %s\n",
                    player.getFirstName(), player.getLastName(),
                    player.getNumber(),
                    df.format(player.getSalary()),
                    player.getTeam().getName()));
        }

        return sb.toString().trim();
    }

}
