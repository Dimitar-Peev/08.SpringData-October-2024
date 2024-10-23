import entities.Employee;

import java.util.Scanner;

public class _08_GetEmployeesWithProject {
    public static void main(String[] args) {
        System.out.println("Enter employee id:");
        Integer employeeId = new Scanner(System.in).nextInt();

        JPA_Util.createEntityManager()
                .createQuery("FROM Employee WHERE id = :employeeId", Employee.class)
                .setParameter("employeeId", employeeId)
                .getSingleResult()
                .printFullNameWithProjectNames();
    }
}
