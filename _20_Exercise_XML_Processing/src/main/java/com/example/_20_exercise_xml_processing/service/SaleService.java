package com.example._20_exercise_xml_processing.service;

import jakarta.xml.bind.JAXBException;

public interface SaleService extends BaseService {

    void importSales();

    void exportSales() throws JAXBException;
}
