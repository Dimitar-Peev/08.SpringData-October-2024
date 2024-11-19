package com.example._19_xml_processing.jaxb;

import com.example._19_xml_processing.jaxb.entities.Address;
import com.example._19_xml_processing.jaxb.entities.Names;
import com.example._19_xml_processing.jaxb.entities.Person;
import com.example._19_xml_processing.jaxb.entities.PhoneNumber;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class Main {
    public static void main(String[] args) throws JAXBException, IOException {
//        Person person = new Person(
//                "Dimitar", "Peev",
//                20,
//                new Address("BG", "Burgas"));
//
//        JAXBContext jaxbContext = JAXBContext.newInstance(Person.class);
//        Marshaller marshaller = jaxbContext.createMarshaller();
//        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
//        marshaller.marshal(person, System.out);
//----------------------------------------------------------------------------------------------------------------------
//        Names names = new Names("Dimitar", "Peev");
//        Person person = new Person(
//                names,
//                20,
//                new Address("BG", "Burgas"));
//
//        JAXBContext jaxbContext = JAXBContext.newInstance(Person.class);
//        Marshaller marshaller = jaxbContext.createMarshaller();
//        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
//        marshaller.marshal(person, System.out);
//----------------------------------------------------------------------------------------------------------------------
        Names names = new Names("Dimitar", "Peev");
        Person person = new Person(
                names,
                20,
                new Address("BG", "Burgas"),
                List.of(new PhoneNumber("0889858687"), new PhoneNumber("0888181818"))
        );

//        JAXBContext nameContext = JAXBContext.newInstance(Names.class);
//        Marshaller nameMarshaller = nameContext.createMarshaller();
//        nameMarshaller.marshal(person, System.out); // Person nor any of its super class is known to this context.

        // from Java object to XML String
        JAXBContext jaxbContext = JAXBContext.newInstance(Person.class);
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.marshal(person, System.out); // write to the console

        OutputStream fileOutput = new FileOutputStream( "src/main/resources/_19_xml_processing/jaxb-output.xml" );
        marshaller.marshal(person, fileOutput); // write to file

        // from XML file to Java object
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        Person xmlFromConsole = (Person) unmarshaller.unmarshal(System.in); // Read from console -> Ctrl+D for "End Of File"

        System.out.println(); // DEBUG

//        Files.readAllLines(Path.of("src/main/resources/person.xml")); // Read from file



    }
}