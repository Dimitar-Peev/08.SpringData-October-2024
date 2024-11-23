package com.example._20_exercise_xml_processing.service.impl;

import com.example._20_exercise_xml_processing.data.dtos.imports.SupplierSeedRootDto;
import com.example._20_exercise_xml_processing.data.entities.Supplier;
import com.example._20_exercise_xml_processing.data.repositories.SupplierRepository;
import com.example._20_exercise_xml_processing.service.SupplierService;
import com.example._20_exercise_xml_processing.utils.XmlParser;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SupplierServiceImpl implements SupplierService {

    private static final String XML_IMPORT_PATH = "src/main/resources/xmls/import/suppliers.xml";

    private final SupplierRepository supplierRepository;
    private final ModelMapper modelMapper;
    private final XmlParser xmlParser;

    @Autowired
    public SupplierServiceImpl(SupplierRepository supplierRepository, ModelMapper modelMapper, XmlParser xmlParser) {
        this.supplierRepository = supplierRepository;
        this.modelMapper = modelMapper;
        this.xmlParser = xmlParser;
    }

    @Override
    public void importSuppliers() {
        SupplierSeedRootDto supplierSeedRootDto = xmlParser.convertFromFile(XML_IMPORT_PATH, SupplierSeedRootDto.class);

        supplierSeedRootDto
                .getSuppliers()
                .forEach(s -> {
                    Supplier supplier = this.modelMapper.map(s, Supplier.class);
                    this.supplierRepository.saveAndFlush(supplier);
                });

        System.out.println("Suppliers imported successfully!");
    }

    @Override
    public boolean isImported() {
        return this.supplierRepository.count() > 0;
    }
}
