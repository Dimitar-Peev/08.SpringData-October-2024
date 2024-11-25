package com.example._20_exercise_xml_processing.utils;

import jakarta.xml.bind.JAXBException;

public interface XmlParser {

    <T> T convertFromFile(String filePath, Class<T> clazz);

    <T> void writeToFile(String filePath, T entity) throws JAXBException;
}
