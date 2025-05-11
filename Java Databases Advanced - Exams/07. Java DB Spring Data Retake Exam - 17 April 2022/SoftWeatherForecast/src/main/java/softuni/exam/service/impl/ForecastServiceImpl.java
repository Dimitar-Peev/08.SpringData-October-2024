package softuni.exam.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.xml.ForecastImportDto;
import softuni.exam.models.dto.xml.ForecastWrapperDto;
import softuni.exam.models.entity.City;
import softuni.exam.models.entity.Forecast;
import softuni.exam.models.entity.DaysOfWeek;
import softuni.exam.repository.CityRepository;
import softuni.exam.repository.ForecastRepository;
import softuni.exam.service.ForecastService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.impl.XmlParserImpl;

import jakarta.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static softuni.exam.constants.Messages.*;
import static softuni.exam.constants.Paths.FORECASTS_PATH;

@RequiredArgsConstructor
@Service
public class ForecastServiceImpl implements ForecastService {

    private final ForecastRepository forecastRepository;
    private final CityRepository cityRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final XmlParserImpl xmlParserImpl;

    @Override
    public boolean areImported() {
        return this.forecastRepository.count() > 0;
    }

    @Override
    public String readForecastsFromFile() throws IOException {
        return Files.readString(Path.of(FORECASTS_PATH));
    }

    @Override
    public String importForecasts() throws IOException, JAXBException {
        StringBuilder sb = new StringBuilder();

        ForecastWrapperDto forecastWrapperDto = xmlParserImpl.fromFile(FORECASTS_PATH, ForecastWrapperDto.class);

        List<ForecastImportDto> forecasts = forecastWrapperDto.getForecasts();

        for (ForecastImportDto forecast : forecasts) {
            Optional<City> cityOpt = cityRepository.findById(forecast.getCity());
            if (cityOpt.isEmpty()) {
                sb.append(INVALID_FORECAST);
                continue;
            }

            boolean isValid = validationUtil.isValid(forecast)
                    && DaysOfWeek.contains(String.valueOf(forecast.getDayOfWeek()))
                    && !forecastRepository.existsForecastByCityAndDayOfWeek(cityOpt.get(), DaysOfWeek.valueOf(String.valueOf(forecast.getDayOfWeek())));

            if (isValid) {
                if (cityRepository.findFirstById(forecast.getCity()).isPresent()) {
                    City firstById = cityRepository.findFirstById(forecast.getCity()).get();
                    Forecast forecastToSave = this.modelMapper.map(forecast, Forecast.class);
                    forecastToSave.setCity(firstById);
                    forecastToSave.setSunrise(LocalTime.parse(forecast.getSunrise(), DateTimeFormatter.ofPattern("HH:mm:ss")));
                    forecastToSave.setSunset(LocalTime.parse(forecast.getSunset(), DateTimeFormatter.ofPattern("HH:mm:ss")));

                    this.forecastRepository.saveAndFlush(forecastToSave);

                    Locale locale = Locale.US;
                    DecimalFormat df = new DecimalFormat("####.00", DecimalFormatSymbols.getInstance(locale));
                    sb.append(String.format(VALID_FORECAST, forecast.getDayOfWeek(), df.format(forecast.getMaxTemperature())));
                }
            } else {
                sb.append(INVALID_FORECAST);
            }

            sb.append(System.lineSeparator());
        }

        return sb.toString().trim();
    }

    @Override
    public String exportForecasts() {

        Set<Forecast> forecasts = this.forecastRepository
                .findAllByDayOfWeekAndCityPopulationLessThanOrderByMaxTemperatureDescIdAsc(DaysOfWeek.SUNDAY, 150_000)
                .orElseThrow(NoSuchElementException::new);

        return forecasts
                .stream()
                .map(Forecast::toString)
                .collect(Collectors.joining(System.lineSeparator()));
    }
}
