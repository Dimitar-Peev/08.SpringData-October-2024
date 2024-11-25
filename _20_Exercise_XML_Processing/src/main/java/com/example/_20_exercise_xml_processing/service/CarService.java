package com.example._20_exercise_xml_processing.service;

import jakarta.xml.bind.JAXBException;

public interface CarService extends BaseService {

    void importCars();

    void exportCarsFromMake() throws JAXBException;

    void exportCarsAndParts() throws JAXBException;
}
