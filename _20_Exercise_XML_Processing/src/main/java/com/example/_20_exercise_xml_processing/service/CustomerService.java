package com.example._20_exercise_xml_processing.service;

import jakarta.xml.bind.JAXBException;

public interface CustomerService extends BaseService {

    void importCustomers();

    void exportOrderedCustomers() throws JAXBException;
}
