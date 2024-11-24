package com.example._20_exercise_xml_processing.service.impl;

import com.example._20_exercise_xml_processing.data.dtos.imports.PartSeedRooDto;
import com.example._20_exercise_xml_processing.data.entities.Part;
import com.example._20_exercise_xml_processing.data.entities.Supplier;
import com.example._20_exercise_xml_processing.data.repositories.PartRepository;
import com.example._20_exercise_xml_processing.data.repositories.SupplierRepository;
import com.example._20_exercise_xml_processing.service.PartService;
import com.example._20_exercise_xml_processing.utils.XmlParser;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Service
public class PartServiceImpl implements PartService {

    private static final String XML_IMPORT_PATH = "src/main/resources/xmls/import/parts.xml";

    private final PartRepository partRepository;
    private final SupplierRepository supplierRepository;
    private final ModelMapper modelMapper;
    private final XmlParser xmlParser;

    @Autowired
    public PartServiceImpl(PartRepository partRepository, SupplierRepository supplierRepository, ModelMapper modelMapper, XmlParser xmlParser) {
        this.partRepository = partRepository;
        this.supplierRepository = supplierRepository;
        this.modelMapper = modelMapper;
        this.xmlParser = xmlParser;
    }

    @Override
    public void importParts() {
        PartSeedRooDto partSeedRooDto = xmlParser.convertFromFile(XML_IMPORT_PATH, PartSeedRooDto.class);

        partSeedRooDto
                .getParts()
                .forEach(p -> {
                    Part part = this.modelMapper.map(p, Part.class);
                    part.setSupplier(getRandomSupplier());

                    this.partRepository.saveAndFlush(part);
                });

        System.out.println("Parts imported successfully!");
    }

    private Supplier getRandomSupplier() {
        int random = ThreadLocalRandom.current().nextInt(1, (int) this.supplierRepository.count() + 1);

        return this.supplierRepository.findById(random).orElse(null);
    }

    @Override
    public boolean isImported() {
        return this.partRepository.count() > 0;
    }
}
