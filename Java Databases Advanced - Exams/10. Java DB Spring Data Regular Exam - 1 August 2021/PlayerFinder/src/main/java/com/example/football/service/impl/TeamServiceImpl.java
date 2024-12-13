package com.example.football.service.impl;

import com.example.football.models.dto.json.TeamSeedDto;
import com.example.football.models.entity.Team;
import com.example.football.repository.TeamRepository;
import com.example.football.repository.TownRepository;
import com.example.football.service.TeamService;
import com.example.football.util.ValidationUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@Service
public class TeamServiceImpl implements TeamService {

    private static final String FILE_PATH = "src/main/resources/files/json/teams.json";

    private final TeamRepository teamRepository;
    private final TownRepository townRepository;
    private final Gson gson;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    public TeamServiceImpl(TeamRepository teamRepository, TownRepository townRepository, Gson gson,
                           ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.teamRepository = teamRepository;
        this.townRepository = townRepository;
        this.gson = gson;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public boolean areImported() {
        return this.teamRepository.count() > 0;
    }

    @Override
    public String readTeamsFileContent() throws IOException {
        return Files.readString(Path.of(FILE_PATH));

    }

    @Override
    public String importTeams() throws IOException {
        StringBuilder output = new StringBuilder();

        TeamSeedDto[] teamSeedDtos = this.gson.fromJson(readTeamsFileContent(), TeamSeedDto[].class);

        Arrays.stream(teamSeedDtos)
                .filter(teamSeedDto -> {
                    boolean isExist = this.teamRepository.existsByName(teamSeedDto.getName());

                    boolean isValid = this.validationUtil.isValid(teamSeedDto) && !isExist;

                    output.append(isValid
                                    ? String.format("Successfully imported Team %s - %d",
                                    teamSeedDto.getName(), teamSeedDto.getFanBase())
                                    : "Invalid Team")
                            .append(System.lineSeparator());

                    return isValid;
                })
                .map(this::newTeam)
                .forEach(teamRepository::save);

        return output.toString().trim();
    }

    private Team newTeam(TeamSeedDto teamSeedDto) {
        Team team = this.modelMapper.map(teamSeedDto, Team.class);
        team.setTown(this.townRepository.findByName(teamSeedDto.getTownName()));
        return team;
    }
}
