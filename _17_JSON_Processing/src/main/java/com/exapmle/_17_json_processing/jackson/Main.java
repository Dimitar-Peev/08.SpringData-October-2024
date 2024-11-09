package com.exapmle._17_json_processing.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Main {
    public static void main(String[] args) throws JsonProcessingException {
        PersonDTO personDTO = new PersonDTO("Dimitar", "Peev", 12, false);
        ObjectMapper objectMapper = new ObjectMapper();

        String encoded = objectMapper.writeValueAsString(personDTO); // takes only the information for which there are getters
        System.out.println(encoded);

        String rawJson = """
                 {"firstName":"Mitko","age":21,"deleted":false}
                """;

        PersonDTO parsed = objectMapper.readValue(rawJson, PersonDTO.class);
        System.out.println(parsed);
    }
}