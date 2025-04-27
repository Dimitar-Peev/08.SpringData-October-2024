package softuni.exam.config;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class LocalDateTimeAdapter extends XmlAdapter<String, LocalDateTime> {

    private final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public String marshal(LocalDateTime localDateTime) {
        return localDateTime.format(dateFormat);
    }

    @Override
    public LocalDateTime unmarshal(String localDateTime) {
        return LocalDateTime.parse(localDateTime, dateFormat);
    }

}