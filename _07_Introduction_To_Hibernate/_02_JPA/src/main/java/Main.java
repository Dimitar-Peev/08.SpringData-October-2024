import entities.Student;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("school");
        EntityManager entityManager = emf.createEntityManager();
//----------------------------------------------------------------------------------------------------------------------
        entityManager.getTransaction().begin();

        Student insert = new Student("Dimitar");
        entityManager.persist(insert); // SQL INSERT

        Student found = entityManager.find(Student.class, 1);
        System.out.println(found);

        List<Student> fromStudent = entityManager.createQuery("FROM Student", Student.class).getResultList();
        fromStudent.forEach(System.out::println);

        Student last = fromStudent.getLast();
        entityManager.remove(last); // SQL DELETE by primary key

        entityManager.getTransaction().commit();

        entityManager.createQuery("FROM Student", Student.class).getResultList().forEach(System.out::println);
//----------------------------------------------------------------------------------------------------------------------
        entityManager.close();
    }
}