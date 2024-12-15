package exam.service.impl;

import com.google.gson.Gson;
import exam.model.dtos.json.LaptopSeedDto;
import exam.model.entities.Laptop;
import exam.repository.LaptopRepository;
import exam.repository.ShopRepository;
import exam.service.LaptopService;
import exam.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

@Service
public class LaptopServiceImpl implements LaptopService {

    private static final String FILE_PATH = "src/main/resources/files/json/laptops.json";

    private final LaptopRepository laptopRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final Gson gson;
    private final ShopRepository shopRepository;

    public LaptopServiceImpl(LaptopRepository laptopRepository, ModelMapper modelMapper, ValidationUtil validationUtil,
                             Gson gson, ShopRepository shopRepository) {
        this.laptopRepository = laptopRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gson = gson;
        this.shopRepository = shopRepository;
    }

    @Override
    public boolean areImported() {
        return this.laptopRepository.count() > 0;
    }

    @Override
    public String readLaptopsFileContent() throws IOException {
        return Files.readString(Path.of(FILE_PATH));
    }

    @Override
    public String importLaptops() throws IOException {
        StringBuilder output = new StringBuilder();

        LaptopSeedDto[] laptopSeedDtos = this.gson.fromJson(readLaptopsFileContent(), LaptopSeedDto[].class);

        System.out.println();
        Arrays.stream(laptopSeedDtos)
                .filter(laptopSeedDto -> {
                    boolean isExistByMac = this.laptopRepository.existsByMacAddress(laptopSeedDto.getMacAddress());

                    boolean isValid = this.validationUtil.isValid(laptopSeedDto) && !isExistByMac;

                    output.append(isValid
                                    ? String.format("Successfully imported Laptop %s - %s - %d - %d",
                                    laptopSeedDto.getMacAddress(),
                                    laptopSeedDto.getCpuSpeed(),
                                    laptopSeedDto.getRam(),
                                    laptopSeedDto.getStorage())
                                    : "Invalid Laptop")
                            .append(System.lineSeparator());

                    return isValid;
                })
                .map(this::newLaptop)
                .forEach(laptopRepository::save);

        return output.toString().trim();
    }

    @Override
    public String exportBestLaptops() {
        List<Laptop> bestLaptops = this.laptopRepository
                .findAllByOrderByCpuSpeedDescRamDescStorageDescMacAddressAsc();

        StringBuilder output = new StringBuilder();
        bestLaptops.forEach(laptop -> {
            output.append(laptop.toString());
            output.append(System.lineSeparator());
        });
        return output.toString();
    }

    private Laptop newLaptop(LaptopSeedDto laptopSeedDto) {
        Laptop laptop = this.modelMapper.map(laptopSeedDto, Laptop.class);
        laptop.setShop(this.shopRepository.findByName(laptopSeedDto.getShop().getName()));
        return laptop;
    }
}
