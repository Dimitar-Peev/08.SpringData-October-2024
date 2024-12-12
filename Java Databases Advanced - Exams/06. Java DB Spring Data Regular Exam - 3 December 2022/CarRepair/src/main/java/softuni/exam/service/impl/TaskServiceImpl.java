package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.TaskExportDto;
import softuni.exam.models.dto.xml.TaskSeedDto;
import softuni.exam.models.dto.xml.TaskSeedRootDto;
import softuni.exam.models.entity.Car;
import softuni.exam.models.entity.Mechanic;
import softuni.exam.models.entity.Part;
import softuni.exam.models.entity.Task;
import softuni.exam.models.entity.enums.CarType;
import softuni.exam.repository.CarRepository;
import softuni.exam.repository.MechanicRepository;
import softuni.exam.repository.PartRepository;
import softuni.exam.repository.TaskRepository;
import softuni.exam.service.TaskService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    private static String TASKS_FILE_PATH = "src/main/resources/files/xml/tasks.xml";

    private final TaskRepository taskRepository;
    private final MechanicRepository mechanicsRepository;
    private final PartRepository partsRepository;
    private final CarRepository carsRepository;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;
    private final XmlParser xmlParser;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository, MechanicRepository mechanicsRepository, PartRepository partsRepository,
                           CarRepository carsRepository, ValidationUtil validationUtil, ModelMapper modelMapper, XmlParser xmlParser) {
        this.taskRepository = taskRepository;
        this.mechanicsRepository = mechanicsRepository;
        this.partsRepository = partsRepository;
        this.carsRepository = carsRepository;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.xmlParser = xmlParser;
    }

    @Override
    public boolean areImported() {
        return this.taskRepository.count() > 0;
    }

    @Override
    public String readTasksFileContent() throws IOException {
        return Files.readString(Path.of(TASKS_FILE_PATH));
    }

    @Override
    public String importTasks() throws IOException, JAXBException {
        StringBuilder output = new StringBuilder();

        TaskSeedRootDto taskSeedRootDto = this.xmlParser.convertFromFile(TASKS_FILE_PATH, TaskSeedRootDto.class);

        taskSeedRootDto.getTasks()
                .stream()
                .filter(taskSeedDto -> {
                    Optional<Mechanic> mechanic = this.mechanicsRepository.findFirstByFirstName(taskSeedDto.getMechanic().getFirstName());
                    Optional<Car> car = this.carsRepository.findById(taskSeedDto.getCar().getId());
                    Optional<Part> part = this.partsRepository.findById(taskSeedDto.getPart().getId());

                    boolean isValid = this.validationUtil.isValid(taskSeedDto)
                            && car.isPresent() && part.isPresent() && mechanic.isPresent();

                    Locale locale = Locale.US;
                    DecimalFormat df = new DecimalFormat("####.00", DecimalFormatSymbols.getInstance(locale));
                    output.append(isValid ? String.format("Successfully imported task %s", df.format(taskSeedDto.getPrice()))
                                    : "Invalid task")
                            .append(System.lineSeparator());
                    return isValid;
                })
                .map(this::newTask)
                .forEach(taskRepository::save);

        return output.toString().trim();
    }

    private Task newTask(TaskSeedDto taskSeedDto) {
        Task taskToSave = modelMapper.map(taskSeedDto, Task.class);

        Optional<Mechanic> mechanic = this.mechanicsRepository.findFirstByFirstName(taskSeedDto.getMechanic().getFirstName());
        Optional<Car> car = this.carsRepository.findById(taskSeedDto.getCar().getId());
        Optional<Part> part = this.partsRepository.findById(taskSeedDto.getPart().getId());

        taskToSave.setMechanic(mechanic.get());
        taskToSave.setPart(part.get());
        taskToSave.setCar(car.get());

        return taskToSave;
    }

    @Override
    public String getCoupeCarTasksOrderByPrice() {

        String collected = this.taskRepository.findAllByCarCarTypeOrderByPriceDesc(CarType.coupe)
                .stream()
                .map(task -> this.modelMapper.map(task, TaskExportDto.class))
                .map(TaskExportDto::toString)
                .collect(Collectors.joining());

        return collected.trim();
    }
}
