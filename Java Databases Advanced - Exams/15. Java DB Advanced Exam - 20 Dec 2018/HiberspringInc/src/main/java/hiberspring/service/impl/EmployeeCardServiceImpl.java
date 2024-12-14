package hiberspring.service.impl;

import com.google.gson.Gson;
import hiberspring.domain.dtos.json.EmployeeCardSeedDto;
import hiberspring.domain.entities.EmployeeCard;
import hiberspring.repository.EmployeeCardRepository;
import hiberspring.service.EmployeeCardService;
import hiberspring.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import static hiberspring.common.GlobalConstants.EMPLOYEE_CARDS_FILE_PATH;
import static hiberspring.common.GlobalConstants.INCORRECT_DATA_MESSAGE;

@Service
public class EmployeeCardServiceImpl implements EmployeeCardService {

    private final EmployeeCardRepository employeeCardRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final Gson gson;

    @Autowired
    public EmployeeCardServiceImpl(EmployeeCardRepository employeeCardRepository, ModelMapper modelMapper,
                                   ValidationUtil validationUtil, Gson gson) {
        this.employeeCardRepository = employeeCardRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gson = gson;
    }

    @Override
    public Boolean employeeCardsAreImported() {
        return this.employeeCardRepository.count() > 0;
    }

    @Override
    public String readEmployeeCardsJsonFile() throws IOException {
        return Files.readString(Path.of(EMPLOYEE_CARDS_FILE_PATH));
    }

    @Override
    public String importEmployeeCards(String employeeCardsFileContent) throws IOException {
        StringBuilder output = new StringBuilder();

        EmployeeCardSeedDto[] employeeCardSeedDtos = this.gson.fromJson(readEmployeeCardsJsonFile(), EmployeeCardSeedDto[].class);

        Arrays.stream(employeeCardSeedDtos)
                .filter(employeeCardSeedDto -> {
                    boolean isExist = this.employeeCardRepository.existsByNumber(employeeCardSeedDto.getNumber());

                    boolean isValid = this.validationUtil.isValid(employeeCardSeedDto) && !isExist;

                    output.append(isValid
                                    ? String.format("Successfully imported Employee Card %s.", employeeCardSeedDto.getNumber())
                                    : INCORRECT_DATA_MESSAGE)
                            .append(System.lineSeparator());

                    return isValid;
                })
                .map(this::newEmployeeCard)
                .forEach(employeeCardRepository::save);

        return output.toString().trim();
    }

    private EmployeeCard newEmployeeCard(EmployeeCardSeedDto branchSeedDto) {
        return this.modelMapper.map(branchSeedDto, EmployeeCard.class);
    }

    @Override
    public EmployeeCard getCardByNumber(String number) {
        return this.employeeCardRepository.findByNumber(number);
    }
}
