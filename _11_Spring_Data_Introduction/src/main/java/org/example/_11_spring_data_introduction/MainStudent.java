package org.example._11_spring_data_introduction;


import org.example._11_spring_data_introduction.entities.Student;
import org.example._11_spring_data_introduction.services.StudentService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class MainStudent implements CommandLineRunner {

    //    @Autowired
    private final StudentService studentService;

    public MainStudent(StudentService studentService) {
        this.studentService = studentService;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Working in Spring");
        studentService.register(new Student("Pesho"));

        Student fromDb = studentService.get(1);
//        studentService.get(5);
    }
}