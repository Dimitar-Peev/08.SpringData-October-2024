package softuni.exam.service.impl;

import jakarta.transaction.Transactional;
import jakarta.xml.bind.JAXBException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.domain.dtos.xml.TeamSeedDto;
import softuni.exam.domain.dtos.xml.TeamSeedRootDto;
import softuni.exam.domain.entities.Team;
import softuni.exam.repository.PictureRepository;
import softuni.exam.repository.TeamRepository;
import softuni.exam.service.PictureService;
import softuni.exam.service.TeamService;
import softuni.exam.util.ValidatorUtil;
import softuni.exam.util.XmlParser;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static softuni.exam.constants.GlobalConstants.TEAMS_FILE_PATH;

@Service
@Transactional
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final PictureService pictureService;
    private final ModelMapper modelMapper;
    private final ValidatorUtil validatorUtil;
    private final XmlParser xmlParser;
    private final PictureRepository pictureRepository;

    @Autowired
    public TeamServiceImpl(TeamRepository teamRepository, PictureService pictureService, ModelMapper modelMapper, ValidatorUtil validatorUtil, XmlParser xmlParser, PictureRepository pictureRepository) {
        this.teamRepository = teamRepository;
        this.pictureService = pictureService;
        this.modelMapper = modelMapper;
        this.validatorUtil = validatorUtil;
        this.xmlParser = xmlParser;
        this.pictureRepository = pictureRepository;
    }

    @Override
    public boolean areImported() {
        return this.teamRepository.count() > 0;
    }

    @Override
    public String readTeamsXmlFile() throws IOException {
        return Files.readString(Path.of(TEAMS_FILE_PATH));
    }

    @Override
    public Team getTeamByName(String name) {
        return this.teamRepository.findByName(name);
    }

    @Override

    public String importTeams() throws JAXBException, FileNotFoundException {
        StringBuilder output = new StringBuilder();

        TeamSeedRootDto teamSeedRootDto = this.xmlParser.convertFromFile(TEAMS_FILE_PATH, TeamSeedRootDto.class);

        teamSeedRootDto.getTeams()
                .stream()
                .filter(teamSeedDto -> {
                    boolean isExist = this.teamRepository.existsTeamByName(teamSeedDto.getName());

                    boolean existPictureByUrl = this.pictureRepository.existsPictureByUrl(teamSeedDto.getPicture().getUrl());

                    boolean isValid = this.validatorUtil.isValid(teamSeedDto) && !isExist && existPictureByUrl;

                    output.append(isValid
                                    ? String.format("Successfully imported - %s", teamSeedDto.getName())
                                    : "Invalid team")
                            .append(System.lineSeparator());

                    return isValid;
                })
                .map(this::newTeam)
                .forEach(teamRepository::save);

        return output.toString().trim();
    }

    private Team newTeam(TeamSeedDto teamSeedDto) {
        Team team = this.modelMapper.map(teamSeedDto, Team.class);
        team.setPicture(this.pictureService.getPictureByUrl(teamSeedDto.getPicture().getUrl()));
        return team;
    }

}
