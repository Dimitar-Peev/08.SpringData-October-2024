import entities.Student;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        Configuration config = new Configuration();
        config.configure("hibernate.cfg.xml");

        SessionFactory sessionFactory = config.buildSessionFactory();
        Session currentSession = sessionFactory.openSession();
//----------------------------------------------------------------------------------------------------------------------
        currentSession.beginTransaction();

        Student insert = new Student();
        insert.setName("Dimitar");
        currentSession.persist(insert); // save

        Student get = currentSession.get(Student.class, 1);
        System.out.println(get);

        currentSession.createQuery("FROM Student", Student.class).list().forEach(System.out::println);

        CriteriaBuilder criteriaBuilder = currentSession.getCriteriaBuilder();
        CriteriaQuery<Student> query = criteriaBuilder.createQuery(Student.class);
        Root<Student> Student_ = query.from(Student.class);
        query.select(Student_).where(criteriaBuilder.like(Student_.get("name"), "D%"));
        List<Student> studentList = currentSession.createQuery(query).getResultList();
        studentList.forEach(s -> System.out.println(s.getName()));

        currentSession.getTransaction().commit();
//----------------------------------------------------------------------------------------------------------------------
        currentSession.close();
        sessionFactory.close();
    }
}
