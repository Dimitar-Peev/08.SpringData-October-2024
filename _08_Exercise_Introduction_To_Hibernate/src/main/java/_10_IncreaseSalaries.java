import entities.Employee;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;

public class _10_IncreaseSalaries {
    private static final List<String> DEPARTMENTS_NAMES_TO_INCREASE_SALARIES =
            List.of("Engineering", "Tool Design", "Marketing", "Information Services");

    public static void main(String[] args) {
        EntityManager entityManager = JPA_Util.createEntityManager();

        entityManager.getTransaction().begin();

        List<Employee> employees = entityManager
                .createQuery("FROM Employee WHERE department.name IN (:departments)", Employee.class)
                .setParameter("departments", DEPARTMENTS_NAMES_TO_INCREASE_SALARIES)
                .getResultList();

        employees.forEach(e -> e.setSalary(e.getSalary().multiply(BigDecimal.valueOf(1.12))));

        entityManager.flush();
        entityManager.getTransaction().commit();
        entityManager.close();

        employees.forEach(e -> System.out.printf("%s %s ($%.2f)%n", e.getFirstName(), e.getLastName(), e.getSalary()));
    }
}
