package hiberspring.service.impl;

import hiberspring.domain.dtos.xml.EmployeeSeedDto;
import hiberspring.domain.dtos.xml.EmployeeSeedRootDto;
import hiberspring.domain.entities.Branch;
import hiberspring.domain.entities.Employee;
import hiberspring.domain.entities.EmployeeCard;
import hiberspring.repository.EmployeeRepository;
import hiberspring.service.BranchService;
import hiberspring.service.EmployeeCardService;
import hiberspring.service.EmployeeService;
import hiberspring.util.ValidationUtil;
import hiberspring.util.XmlParser;
import jakarta.xml.bind.JAXBException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import static hiberspring.common.GlobalConstants.*;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final XmlParser xmlParser;
    private final BranchService branchService;
    private final EmployeeCardService employeeCardService;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository, ModelMapper modelMapper,
                               ValidationUtil validationUtil, XmlParser xmlParser,
                               BranchService branchService, EmployeeCardService employeeCardService) {
        this.employeeRepository = employeeRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.xmlParser = xmlParser;
        this.branchService = branchService;
        this.employeeCardService = employeeCardService;
    }

    @Override
    public Boolean employeesAreImported() {
        return this.employeeRepository.count() > 0;
    }

    @Override
    public String readEmployeesXmlFile() throws IOException {
        return Files.readString(Path.of(EMPLOYEES_FILE_PATH));
    }

    @Override
    public String importEmployees() throws JAXBException, FileNotFoundException {
        StringBuilder output = new StringBuilder();

        EmployeeSeedRootDto employeeSeedRootDto = this.xmlParser.convertFromFile(EMPLOYEES_FILE_PATH, EmployeeSeedRootDto.class);

        employeeSeedRootDto.getEmployees()
                .stream()
                .filter(employeeSeedDto -> {
                    Employee isExist = this.employeeRepository.findByFirstNameAndLastNameAndPosition(
                            employeeSeedDto.getFirstName(), employeeSeedDto.getLastName(), employeeSeedDto.getPosition());

                    boolean isExistByCard = employeeRepository.existsByCardNumber(employeeSeedDto.getCard());

                    boolean isValid = this.validationUtil.isValid(employeeSeedDto) && isExist == null && !isExistByCard;

                    output.append(isValid ? String.format("Successfully imported Employee %s %s.",
                                    employeeSeedDto.getFirstName(), employeeSeedDto.getLastName())
                                    : INCORRECT_DATA_MESSAGE)
                            .append(System.lineSeparator());
                    return isValid;
                })
                .map(this::newEmployee)
                .forEach(employeeRepository::save);

        return output.toString().trim();
    }

    private Employee newEmployee(EmployeeSeedDto employeeSeedDto) {
        Employee employee = this.modelMapper.map(employeeSeedDto, Employee.class);

        Branch branch = this.branchService.getBranchByName(employeeSeedDto.getBranch());
        EmployeeCard card = this.employeeCardService.getCardByNumber(employeeSeedDto.getCard());
        employee.setBranch(branch);
        employee.setCard(card);

        return employee;
    }

    @Override
    public String exportProductiveEmployees() {
        StringBuilder output = new StringBuilder();

        List<Employee> allByBranchWithMoreThanOneProduct = employeeRepository.findAllByBranchWithMoreThanOneProduct();

        output.append(allByBranchWithMoreThanOneProduct
                .stream()
                .map(Employee::toString)
                .collect(Collectors.joining("-------------------------\n")));

        return output.toString().trim();
    }

}
