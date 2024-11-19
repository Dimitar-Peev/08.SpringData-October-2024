package com.example._19_xml_processing.jaxb.entities;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "person")
@XmlAccessorType(XmlAccessType.FIELD)
public class Person {
//    @XmlElement(name = "firstName")
//    private String firstName;
//
//    @XmlElement
//    private String lastName;

    @XmlElement
    private Names names;

    @XmlElement
    private int age;

    @XmlElement
    private Address address;

    @XmlElementWrapper // <phoneNumbers>     </phoneNumbers>
    @XmlElement(name = "singlePhone") //  <singlePhone>      </singlePhone>
    List<PhoneNumber> phoneNumbers;

    public Person() {
    }

//    public Person(String firstName, String lastName, int age, Address address) {
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.age = age;
//        this.address = address;
//    }

//    public Person(Names names, int age, Address address) {
//        this.names = names;
//        this.age = age;
//        this.address = address;
//    }

        public Person(Names names, int age, Address address, List<PhoneNumber> phoneNumbers) {
        this.names = names;
        this.age = age;
        this.address = address;
        this.phoneNumbers = phoneNumbers;
    }
}