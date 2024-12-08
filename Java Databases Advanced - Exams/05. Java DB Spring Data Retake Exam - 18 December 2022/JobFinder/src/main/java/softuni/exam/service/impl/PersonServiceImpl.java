package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.json.PersonSeedDto;
import softuni.exam.models.entity.Person;
import softuni.exam.models.entity.StatusType;
import softuni.exam.repository.CountryRepository;
import softuni.exam.repository.PersonRepository;
import softuni.exam.service.PersonService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@Service
public class PersonServiceImpl implements PersonService {

    private static final String FILE_PATH = "src/main/resources/files/json/people.json";

    private final PersonRepository personRepository;
    private final CountryRepository countryRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final Gson gson;

    @Autowired
    public PersonServiceImpl(PersonRepository personRepository, CountryRepository countryRepository,
                             ModelMapper modelMapper, ValidationUtil validationUtil, Gson gson) {
        this.personRepository = personRepository;
        this.countryRepository = countryRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gson = gson;
    }

    @Override
    public boolean areImported() {
        return this.personRepository.count() > 0;
    }

    @Override
    public String readPeopleFromFile() throws IOException {
        return Files.readString(Path.of(FILE_PATH));
    }

    @Override
    public String importPeople() throws IOException {
        StringBuilder output = new StringBuilder();

        PersonSeedDto[] personSeedDtos = this.gson.fromJson(readPeopleFromFile(), PersonSeedDto[].class);

        Arrays.stream(personSeedDtos)
                .filter(personSeedDto -> {
                    boolean doesPersonExist = this.personRepository.existsByFirstNameOrEmailOrPhone(
                            personSeedDto.getFirstName(), personSeedDto.getEmail(), personSeedDto.getPhone());

                    boolean isValid = this.validationUtil.isValid(personSeedDto) && !doesPersonExist;

                    output.append(isValid
                                    ? String.format("Successfully imported person %s %s",
                                    personSeedDto.getFirstName(), personSeedDto.getLastName())
                                    : "Invalid person")
                            .append(System.lineSeparator());

                    return isValid;
                })
                .map(this::newPerson)
                .forEach(personRepository::save);

        return output.toString().trim();
    }

    private Person newPerson(PersonSeedDto personSeedDto) {
        Person person = this.modelMapper.map(personSeedDto, Person.class);

        person.setStatusType(StatusType.valueOf(personSeedDto.getStatusType()));
        person.setCountry(this.countryRepository.findCountryById(personSeedDto.getCountry()));

        return person;
    }
}
