package com.example._19_xml_processing.jackson;

import com.example._19_xml_processing.jackson.entities.ContactInfo;
import com.example._19_xml_processing.jackson.entities.Message;
import com.example._19_xml_processing.jackson.entities.User;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        XmlMapper xmlMapper = new XmlMapper();

        xmlMapper.enable(SerializationFeature.INDENT_OUTPUT); // alignment

        User user = new User("user", "pass", 20,
                new ContactInfo("email@mail.com", "0888"),
                List.of(new Message("first"), new Message("second"))
        );

        xmlMapper.writeValue(System.out, user);  // Write to console
        xmlMapper.writeValue(new File("src/main/resources/_19_xml_processing/jackson-output.xml"), user);  // Write to file

        User fromXml = xmlMapper.readValue(System.in, User.class); // Read from console

        System.out.println(); // DEBUG
    }
}