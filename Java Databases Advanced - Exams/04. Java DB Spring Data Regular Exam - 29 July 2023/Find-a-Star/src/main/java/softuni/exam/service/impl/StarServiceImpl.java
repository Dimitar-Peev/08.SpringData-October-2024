package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.json.StarSeedDto;
import softuni.exam.models.entity.Star;
import softuni.exam.models.entity.StarType;
import softuni.exam.repository.ConstellationRepository;
import softuni.exam.repository.StarRepository;
import softuni.exam.service.StarService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StarServiceImpl implements StarService {
    private static final Locale locale = Locale.US;
    private static final DecimalFormat df = new DecimalFormat("####.00", DecimalFormatSymbols.getInstance(locale));

    private static final String FILE_PATH = "src/main/resources/files/json/stars.json";

    private final StarRepository starRepository;
    private final ConstellationRepository constellationRepository;
    private final Gson gson;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    @Autowired
    public StarServiceImpl(StarRepository starRepository, ConstellationRepository constellationRepository, Gson gson, ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.starRepository = starRepository;
        this.constellationRepository = constellationRepository;
        this.gson = gson;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public boolean areImported() {
        return this.starRepository.count() > 0;
    }

    @Override
    public String readStarsFileContent() throws IOException {
        return new String(Files.readAllBytes(Path.of(FILE_PATH)));
    }

    @Override
    public String importStars() throws IOException {
        StringBuilder sb = new StringBuilder();

        StarSeedDto[] starSeedDtos = this.gson.fromJson(readStarsFileContent(), StarSeedDto[].class);

        for (StarSeedDto starSeedDto : starSeedDtos) {
            Optional<Star> optional = this.starRepository.findByName(starSeedDto.getName());

            if (this.validationUtil.isValid(starSeedDto) && optional.isEmpty()) {
                Star star = this.modelMapper.map(starSeedDto, Star.class);
                star.setStarType(StarType.valueOf(starSeedDto.getStarType()));
                star.setConstellation(this.constellationRepository.getById(starSeedDto.getConstellation()));

                this.starRepository.saveAndFlush(star);
                sb.append(String.format("Successfully imported star %s - %s light years", star.getName(), df.format(star.getLightYears())));
            } else {
                sb.append("Invalid star");
            }

            sb.append(System.lineSeparator());
        }

        return sb.toString().trim();
    }

    @Override
    public String exportStars() {

        return this.starRepository
                .findAllByStarTypeOrderByLightYears()
                .stream()
                .map(s -> String.format("Star: %s%n" +
                                "   *Distance: %s light years%n" +
                                "   **Description: %s%n" +
                                "   ***Constellation: %s%n",
                        s.getName(), df.format(s.getLightYears()), s.getDescription(), s.getConstellation().getName()))
                .collect(Collectors.joining());
    }
}
