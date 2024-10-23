import entities.Employee;

import javax.persistence.EntityManager;
import java.util.Scanner;

public class _03_ContainsEmployee {
    public static void main(String[] args) {
        EntityManager entityManager = JPA_Util.createEntityManager();

        System.out.println("Enter employee name: ");
        Scanner scanner = new Scanner(System.in);
        String fullName = scanner.nextLine();

        entityManager.getTransaction().begin();

        String isEmployeePresented = entityManager
                .createQuery("FROM Employee WHERE CONCAT_WS(' ', firstName, lastName) = :fullName", Employee.class)
                .setParameter("fullName", fullName)
                .getResultList()
                .isEmpty() ? "No" : "Yes";

        entityManager.getTransaction().commit();

        System.out.println(isEmployeePresented);
    }
}
