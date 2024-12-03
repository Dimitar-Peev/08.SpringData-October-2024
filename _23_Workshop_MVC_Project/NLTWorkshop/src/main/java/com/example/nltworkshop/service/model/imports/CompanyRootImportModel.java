package com.example.nltworkshop.service.model.imports;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.Set;

@JacksonXmlRootElement(localName = "companies")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
//@XmlRootElement(name = "companies")
//@XmlAccessorType(XmlAccessType.FIELD)
public class CompanyRootImportModel {

    @JacksonXmlProperty(localName = "company")
//    @XmlElement(name = "company")
    private Set<CompanyImportModel> companyImportModels;

    public CompanyRootImportModel() {
    }

    public CompanyRootImportModel(Set<CompanyImportModel> companyImportModels) {
        this.companyImportModels = companyImportModels;
    }

    public Set<CompanyImportModel> getCompanyImportModels() {
        return companyImportModels;
    }

    public void setCompanyImportModels(Set<CompanyImportModel> companyImportModels) {
        this.companyImportModels = companyImportModels;
    }
}