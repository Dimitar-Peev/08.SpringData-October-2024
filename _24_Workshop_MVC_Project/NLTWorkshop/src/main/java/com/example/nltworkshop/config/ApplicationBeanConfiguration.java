package com.example.nltworkshop.config;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
public class ApplicationBeanConfiguration {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.addConverter(context -> LocalDate.parse(context.getSource()), String.class, LocalDate.class);

        return modelMapper;
    }

    @Bean
    public XmlMapper xmlMapper() {
        return new XmlMapper();
    }
}