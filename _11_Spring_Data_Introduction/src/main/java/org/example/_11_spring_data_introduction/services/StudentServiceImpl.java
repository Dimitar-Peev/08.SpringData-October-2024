package org.example._11_spring_data_introduction.services;

import org.example._11_spring_data_introduction.entities.Student;
import org.example._11_spring_data_introduction.repositories.StudentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public void register(Student student) {
        System.out.println("Register the student");
        studentRepository.save(student);
    }

    @Override
    public void expel(Student student) {
        studentRepository.delete(student);
    }

    @Override
    public Student get(int id) {
        Optional<Student> byId = studentRepository.findById(id);

//        return byId.orElseThrow(EntityNotFoundException::new);

        if (byId.isEmpty()) {
            throw new EntityNotFoundException();
        }

        return byId.get();

    }
}
