package org.example._11_spring_data_introduction.services;

import org.example._11_spring_data_introduction.entities.Student;

public interface StudentService {
    void register(Student student);

    void expel(Student student);

    Student get(int id);
}