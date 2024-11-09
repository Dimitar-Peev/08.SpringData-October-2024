package com.exapmle._17_json_processing.controllers;

import com.exapmle._17_json_processing.entities.dtos.PersonDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

//    private final PersonService personService;
//
//    public HomeController(PersonService personService) {
//        this.personService = personService;
//    }

    @GetMapping("/home") //
    public void handleHome() {
        System.out.println("Handled");
    }

    @GetMapping(value = "/person")
    public PersonDTO getPerson() {
        return new PersonDTO("Dimitar", "Peev", 20); // needs getters and setters
    }

    public void updatePerson(PersonDTO person) {

    }
}