package softuni.exam.service.impl;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.json.CarSeedDto;
import softuni.exam.models.entity.Car;
import softuni.exam.repository.CarRepository;
import softuni.exam.service.CarService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

@AllArgsConstructor
@Service
public class CarServiceImpl implements CarService {

    private static final String FILE_PATH = "src/main/resources/files/json/cars.json";

    private final CarRepository carRepository;
    private final Gson gson;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    @Override
    public boolean areImported() {
        return this.carRepository.count() > 0;
    }

    @Override
    public String readCarsFileContent() throws IOException {
        return Files.readString(Path.of(FILE_PATH));
    }

    @Override
    public String importCars() throws IOException {
        StringBuilder sb = new StringBuilder();

        CarSeedDto[] carSeedDtos = this.gson.fromJson(readCarsFileContent(), CarSeedDto[].class);

        for (CarSeedDto carSeedDto : carSeedDtos) {
            if (this.validationUtil.isValid(carSeedDto)) {
                this.carRepository.save(this.modelMapper.map(carSeedDto, Car.class));
                sb.append(String.format("Successfully imported car - %s - %s", carSeedDto.getMake(), carSeedDto.getModel()));
            } else {
                sb.append("Invalid car");
            }
            sb.append(System.lineSeparator());
        }

        return sb.toString().trim();
    }

    @Override
    public String getCarsOrderByPicturesCountThenByMake() {
        StringBuilder sb = new StringBuilder();

        Set<Car> cars = this.carRepository.exportCars();

        cars.forEach(sb::append);

        return sb.toString().trim();
    }

    @Override
    public Car findById(long car) {
        return this.carRepository.findById(car).get();
    }
}
