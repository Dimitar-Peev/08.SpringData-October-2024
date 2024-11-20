package com.example._19_xml_processing.jackson.entities;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class ContactInfo {
    @JacksonXmlProperty
    private String email;

    @JacksonXmlProperty(isAttribute = true)
    private String phone;

    public ContactInfo() {}

    public ContactInfo(String email, String phone) {
        this.email = email;
        this.phone = phone;
    }
}