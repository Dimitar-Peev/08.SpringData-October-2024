package com.example.nltworkshop.service.model.imports;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class CompanyImportModel {

    @JacksonXmlProperty(localName = "name", isAttribute = true)
    private String name;

    public CompanyImportModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}