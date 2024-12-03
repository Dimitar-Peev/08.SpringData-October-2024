package com.example.nltworkshop.service.model.imports;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import javax.xml.bind.annotation.XmlAttribute;

@JacksonXmlRootElement(localName = "company")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
//@XmlRootElement(name = "company")
//@XmlAccessorType(XmlAccessType.FIELD)
public class CompanyImportModel {

    @XmlAttribute
    private String name;

    public CompanyImportModel() {
    }

    public CompanyImportModel(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}