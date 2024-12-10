package sofuni.exam.service.Impl;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import jakarta.xml.bind.JAXBException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sofuni.exam.models.dto.MoonSeedDto;
import sofuni.exam.models.dto.MoonSeedRootDto;
import sofuni.exam.models.entity.Moon;
import sofuni.exam.models.enums.Type;
import sofuni.exam.repository.DiscovererRepository;
import sofuni.exam.repository.MoonRepository;
import sofuni.exam.repository.PlanetRepository;
import sofuni.exam.service.MoonService;
import sofuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
public class MoonServiceImpl implements MoonService {

    private static final String FILE_PATH = "src/main/resources/files/xml/moons.xml";

    private final MoonRepository moonRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final XmlMapper xmlMapper;
    private final DiscovererRepository discovererRepository;
    private final PlanetRepository planetRepository;

    @Autowired
    public MoonServiceImpl(MoonRepository moonRepository, ModelMapper modelMapper, ValidationUtil validationUtil,
                           XmlMapper xmlMapper, DiscovererRepository discovererRepository, PlanetRepository planetRepository) {
        this.moonRepository = moonRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.xmlMapper = xmlMapper;
        this.discovererRepository = discovererRepository;
        this.planetRepository = planetRepository;
    }

    @Override
    public boolean areImported() {
        return this.moonRepository.count() > 0;
    }

    @Override
    public String readMoonsFileContent() throws IOException {
        return Files.readString(Path.of(FILE_PATH));
    }

    @Override
    public String importMoons() throws IOException, JAXBException {
        StringBuilder output = new StringBuilder();

        MoonSeedRootDto moonSeedRootDto = this.xmlMapper.readValue(readMoonsFileContent(), MoonSeedRootDto.class);

        moonSeedRootDto.getMoons()
                .stream()
                .filter(moonSeedDto -> {
                    boolean existsByName = this.moonRepository.existsByName(moonSeedDto.getName());

                    boolean isValid = this.validationUtil.isValid(moonSeedDto) && !existsByName;

                    output.append(isValid ? "Successfully imported moon " + moonSeedDto.getName()
                                    : "Invalid moon")
                            .append(System.lineSeparator());
                    return isValid;
                })
                .map(this::newMoon)
                .forEach(moonRepository::save);

        return output.toString().trim();
    }

    private Moon newMoon(MoonSeedDto moonSeedDto) {
        Moon moon = this.modelMapper.map(moonSeedDto, Moon.class);

        moon.setDiscoverer(this.discovererRepository.findDiscovererById(Long.valueOf(moonSeedDto.getDiscovererId())));
        moon.setPlanet(this.planetRepository.findPlanetById(Long.valueOf(moonSeedDto.getDiscovererId())));

        return moon;
    }

    @Override
    public String exportMoons() {
        List<Moon> moons = this.moonRepository
                .findMoonByPlanetTypeAndRadiusGreaterThanEqualAndRadiusLessThanEqualOrderByName(Type.valueOf("GAS_GIANT"), 700D, 2000D);

        StringBuilder output = new StringBuilder();
        moons.forEach(moon -> output.append(moon.toString()).append(System.lineSeparator()));
        return output.toString().trim();
    }
}
