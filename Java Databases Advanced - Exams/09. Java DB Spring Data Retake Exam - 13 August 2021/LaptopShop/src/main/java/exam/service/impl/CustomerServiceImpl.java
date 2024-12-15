package exam.service.impl;

import com.google.gson.Gson;
import exam.model.dtos.json.CustomerSeedDto;
import exam.model.entities.Customer;
import exam.repository.CustomerRepository;
import exam.repository.TownRepository;
import exam.service.CustomerService;
import exam.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@Service
public class CustomerServiceImpl implements CustomerService {

    private static final String FILE_PATH = "src/main/resources/files/json/customers.json";

    private final CustomerRepository customerRepository;
    private final TownRepository townRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final Gson gson;

    public CustomerServiceImpl(CustomerRepository customerRepository, TownRepository townRepository,
                               ModelMapper modelMapper, ValidationUtil validationUtil, Gson gson) {
        this.customerRepository = customerRepository;
        this.townRepository = townRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gson = gson;
    }

    @Override
    public boolean areImported() {
        return this.customerRepository.count() > 0;
    }

    @Override
    public String readCustomersFileContent() throws IOException {
        return Files.readString(Path.of(FILE_PATH));
    }

    @Override
    public String importCustomers() throws IOException {
        StringBuilder output = new StringBuilder();

        CustomerSeedDto[] customerSeedDtos = this.gson.fromJson(readCustomersFileContent(), CustomerSeedDto[].class);

        Arrays.stream(customerSeedDtos)
                .filter(customerSeedDto -> {
                    boolean isValid = this.validationUtil.isValid(customerSeedDto)
                            && !this.customerRepository.existsByEmail(customerSeedDto.getEmail());

                    output.append(isValid
                                    ? String.format("Successfully imported Customer %s %s - %s",
                                    customerSeedDto.getFirstName(),
                                    customerSeedDto.getLastName(),
                                    customerSeedDto.getEmail())
                                    : "Invalid Customer")
                            .append(System.lineSeparator());

                    return isValid;
                })
                .map(this::newCustomer)
                .forEach(customerRepository::save);

        return output.toString().trim();
    }

    private Customer newCustomer(CustomerSeedDto customerSeedDto) {
        Customer customer = this.modelMapper.map(customerSeedDto, Customer.class);
        customer.setTown(this.townRepository.findByName(customerSeedDto.getTown().getName()));
        return customer;
    }
}
