package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.xml.CarSeedDto;
import softuni.exam.models.dto.xml.CarSeedRootDto;
import softuni.exam.models.entity.Car;
import softuni.exam.repository.CarRepository;
import softuni.exam.service.CarService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class CarServiceImpl implements CarService {

    private static String CARS_FILE_PATH = "src/main/resources/files/xml/cars.xml";

    private final CarRepository carRepository;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;
    private final XmlParser xmlParser;

    @Autowired
    public CarServiceImpl(CarRepository carRepository, ValidationUtil validationUtil,
                          ModelMapper modelMapper, XmlParser xmlParser) {
        this.carRepository = carRepository;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.xmlParser = xmlParser;
    }

    @Override
    public boolean areImported() {
        return this.carRepository.count() > 0;
    }

    @Override
    public String readCarsFromFile() throws IOException {
        return Files.readString(Path.of(CARS_FILE_PATH));
    }

    @Override
    public String importCars() throws IOException, JAXBException {
        StringBuilder output = new StringBuilder();

        CarSeedRootDto carSeedRootDto = this.xmlParser.convertFromFile(CARS_FILE_PATH, CarSeedRootDto.class);

        carSeedRootDto.getCars()
                .stream()
                .filter(carSeedDto -> {
                    boolean isExist = carRepository.existsByPlateNumber(carSeedDto.getPlateNumber());

                    boolean isValid = validationUtil.isValid(carSeedDto) && !isExist;

                    output.append(isValid ? String.format("Successfully imported car %s - %s",
                                    carSeedDto.getCarMake(), carSeedDto.getCarModel())
                                    : "Invalid car")
                            .append(System.lineSeparator());
                    return isValid;
                })
                .map(this::newCar)
                .forEach(carRepository::save);

        return output.toString().trim();
    }

    private Car newCar(CarSeedDto carSeedDto) {
        return modelMapper.map(carSeedDto, Car.class);
    }
}