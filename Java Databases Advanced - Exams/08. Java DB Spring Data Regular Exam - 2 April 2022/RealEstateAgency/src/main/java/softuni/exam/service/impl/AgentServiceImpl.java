package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.json.AgentSeedDto;
import softuni.exam.models.entity.Agent;
import softuni.exam.repository.AgentRepository;
import softuni.exam.repository.TownRepository;
import softuni.exam.service.AgentService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@Service
public class AgentServiceImpl implements AgentService {

    private static final String FILE_PATH = "src/main/resources/files/json/agents.json";

    private final AgentRepository agentRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final Gson gson;
    private final TownRepository townRepository;

    @Autowired
    public AgentServiceImpl(AgentRepository agentRepository, ModelMapper modelMapper, ValidationUtil validationUtil,
                            Gson gson, TownRepository townRepository) {
        this.agentRepository = agentRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gson = gson;
        this.townRepository = townRepository;
    }

    @Override
    public boolean areImported() {
        return this.agentRepository.count() > 0;
    }

    @Override
    public String readAgentsFromFile() throws IOException {
        return Files.readString(Path.of(FILE_PATH));
    }

    @Override
    public String importAgents() throws IOException {
        StringBuilder sb = new StringBuilder();

        AgentSeedDto[] agentSeedDtos = this.gson.fromJson(readAgentsFromFile(), AgentSeedDto[].class);

        Arrays.stream(agentSeedDtos)
                .filter(agentSeedDto -> {
                    boolean isExist = this.agentRepository.existsByFirstName(agentSeedDto.getFirstName());

                    boolean isValid = this.validationUtil.isValid(agentSeedDto) && !isExist;

                    sb.append(isValid
                                    ? String.format("Successfully imported agent - %s %s",
                                    agentSeedDto.getFirstName(), agentSeedDto.getLastName())
                                    : "Invalid agent")
                            .append(System.lineSeparator());

                    return isValid;
                })
                .map(this::newAgent)
                .forEach(agentRepository::save);

        return sb.toString().trim();
    }

    private Agent newAgent(AgentSeedDto agentSeedDto) {
        Agent agent = modelMapper.map(agentSeedDto, Agent.class);
        agent.setTown(this.townRepository.findByTownName(agentSeedDto.getTown()));
        return agent;
    }
}
