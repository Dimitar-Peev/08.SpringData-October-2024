package hiberspring.service.impl;

import com.google.gson.Gson;
import hiberspring.domain.dtos.json.BranchSeedDto;
import hiberspring.domain.entities.Branch;
import hiberspring.domain.entities.Town;
import hiberspring.repository.BranchRepository;
import hiberspring.service.BranchService;
import hiberspring.service.TownService;
import hiberspring.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import static hiberspring.common.GlobalConstants.BRANCHES_FILE_PATH;
import static hiberspring.common.GlobalConstants.INCORRECT_DATA_MESSAGE;

@Service
public class BranchServiceImpl implements BranchService {

    private final BranchRepository branchRepository;
    private final TownService townService;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final Gson gson;

    @Autowired
    public BranchServiceImpl(BranchRepository branchRepository, TownService townService, ModelMapper modelMapper,
                             ValidationUtil validationUtil, Gson gson) {
        this.branchRepository = branchRepository;
        this.townService = townService;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gson = gson;
    }

    @Override
    public Boolean branchesAreImported() {
        return this.branchRepository.count() > 0;
    }

    @Override
    public String readBranchesJsonFile() throws IOException {
        return Files.readString(Path.of(BRANCHES_FILE_PATH));
    }

    @Override
    public String importBranches(String branchesFileContent) throws IOException {
        StringBuilder output = new StringBuilder();

        BranchSeedDto[] branchSeedDtos = this.gson.fromJson(readBranchesJsonFile(), BranchSeedDto[].class);

        Arrays.stream(branchSeedDtos)
                .filter(branchSeedDto -> {
                    boolean isExist = this.branchRepository.existsByName(branchSeedDto.getName());

                    boolean isValid = this.validationUtil.isValid(branchSeedDto) && !isExist;

                    output.append(isValid
                                    ? String.format("Successfully imported Branch %s.", branchSeedDto.getName())
                                    : INCORRECT_DATA_MESSAGE)
                            .append(System.lineSeparator());

                    return isValid;
                })
                .map(this::newBranch)
                .forEach(branchRepository::save);

        return output.toString().trim();
    }

    private Branch newBranch(BranchSeedDto branchSeedDto) {
        Branch branch = this.modelMapper.map(branchSeedDto, Branch.class);
        Town town = this.townService.findTownByName(branchSeedDto.getTown());
        branch.setTown(town);
        return branch;
    }

    @Override
    public Branch getBranchByName(String branchName) {
        return this.branchRepository.findByName(branchName);
    }
}
