package com.example.football.service.impl;

import com.example.football.models.dto.xml.StatSeedRootDto;
import com.example.football.models.entity.Stat;
import com.example.football.repository.StatRepository;
import com.example.football.service.StatService;
import com.example.football.util.ValidationUtil;
import com.example.football.util.XmlParser;
import jakarta.xml.bind.JAXBException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

@Service
public class StatServiceImpl implements StatService {

    private static final String FILE_PATH = "src/main/resources/files/xml/stats.xml";
    private static final Locale locale = Locale.US;
    private static final DecimalFormat df = new DecimalFormat("####.00", DecimalFormatSymbols.getInstance(locale));

    private final StatRepository statRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final XmlParser xmlParser;

    public StatServiceImpl(StatRepository statRepository, ModelMapper modelMapper,
                           ValidationUtil validationUtil, XmlParser xmlParser) {
        this.statRepository = statRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.xmlParser = xmlParser;
    }

    @Override
    public boolean areImported() {
        return this.statRepository.count() > 0;
    }

    @Override
    public String readStatsFileContent() throws IOException {
        return Files.readString(Path.of(FILE_PATH));
    }

    @Override
    public String importStats() throws JAXBException, IOException {
        StringBuilder output = new StringBuilder();

        StatSeedRootDto statSeedRootDto = this.xmlParser.convertFromFile(FILE_PATH, StatSeedRootDto.class);

        statSeedRootDto.getStats()
                .stream()
                .filter(statSeedDto -> {
                    boolean isExist = this.statRepository.existsByShootingAndPassingAndEndurance(
                            statSeedDto.getShooting(), statSeedDto.getPassing(), statSeedDto.getEndurance());

                    boolean isValid = this.validationUtil.isValid(statSeedDto) && !isExist;

                    output.append(isValid ? String.format("Successfully imported Stat %s - %s - %s",
                                    df.format(statSeedDto.getShooting()),
                                    df.format(statSeedDto.getPassing()),
                                    df.format(statSeedDto.getEndurance()))
                                    : "Invalid Stat")
                            .append(System.lineSeparator());
                    return isValid;
                })
                .map(statSeedDto -> this.modelMapper.map(statSeedDto, Stat.class))
                .forEach(statRepository::save);

        return output.toString().trim();
    }
}
