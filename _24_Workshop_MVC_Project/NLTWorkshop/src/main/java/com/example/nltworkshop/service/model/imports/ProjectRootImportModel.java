package com.example.nltworkshop.service.model.imports;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.Set;

@JacksonXmlRootElement(localName = "projects")
//@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY) // by default
public class ProjectRootImportModel {

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "project")
    private Set<ProjectImportModel> projects;

    public ProjectRootImportModel() {
    }

    public ProjectRootImportModel(Set<ProjectImportModel> projectImportModels) {
        this.projects = projectImportModels;
    }

    public Set<ProjectImportModel> getProjectImportModels() {
        return projects;
    }

    public void setProjectImportModels(Set<ProjectImportModel> projectImportModels) {
        this.projects = projectImportModels;
    }
}