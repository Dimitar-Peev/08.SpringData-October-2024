package com.example._20_exercise_xml_processing.service.impl;

import com.example._20_exercise_xml_processing.data.dtos.exports.CustomerExportDto;
import com.example._20_exercise_xml_processing.data.dtos.exports.CustomerExportRootDto;
import com.example._20_exercise_xml_processing.data.dtos.imports.CustomerSeedRootDto;
import com.example._20_exercise_xml_processing.data.entities.Customer;
import com.example._20_exercise_xml_processing.data.repositories.CustomerRepository;
import com.example._20_exercise_xml_processing.service.CustomerService;
import com.example._20_exercise_xml_processing.utils.XmlParser;
import jakarta.xml.bind.JAXBException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    private static final String XML_IMPORT_PATH = "src/main/resources/xmls/import/customers.xml";
    private static final String XML_EXPORT_PATH = "src/main/resources/xmls/export/ordered-customers.xml";

    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapper;
    private final XmlParser xmlParser;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository, ModelMapper modelMapper, XmlParser xmlParser) {
        this.customerRepository = customerRepository;
        this.modelMapper = modelMapper;
        this.xmlParser = xmlParser;
    }

    @Override
    public void importCustomers() {
        CustomerSeedRootDto customerSeedRootDto = xmlParser.convertFromFile(XML_IMPORT_PATH, CustomerSeedRootDto.class);

        customerSeedRootDto
                .getCustomers()
                .forEach(c -> {
                    Customer customer = this.modelMapper.map(c, Customer.class);
//                    customer.setBirthDate(LocalDate.parse(c.getBirthDate().split("T")[0]));
                    this.customerRepository.saveAndFlush(customer);
                });
        System.out.println("Customers imported successfully!");
    }

    @Override
    public void exportOrderedCustomers() throws JAXBException {
        LinkedHashSet<CustomerExportDto> customerSet =
                this.customerRepository.findAllByOrderByBirthDateAscIsYoungDriverDesc()
                        .stream()
                        .map(c -> this.modelMapper.map(c, CustomerExportDto.class))
                        .collect(Collectors.toCollection(LinkedHashSet::new));

        CustomerExportRootDto customerExportRootDto = new CustomerExportRootDto(customerSet);
        xmlParser.writeToFile(XML_EXPORT_PATH, customerExportRootDto);
    }

    @Override
    public boolean isImported() {
        return this.customerRepository.count() > 0;
    }
}
