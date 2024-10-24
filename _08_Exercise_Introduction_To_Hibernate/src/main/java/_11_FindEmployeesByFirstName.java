import entities.Employee;

import java.util.Scanner;

public class _11_FindEmployeesByFirstName {
    public static void main(String[] args) {
        System.out.println("Enter pattern: ");
        Scanner scanner = new Scanner(System.in);
        String pattern = scanner.nextLine();

        JPA_Util.createEntityManager()
                .createQuery("FROM Employee WHERE firstName LIKE CONCAT(:pattern, '%')", Employee.class)
                .setParameter("pattern", pattern)
                .getResultList()
                .forEach(e -> System.out.printf("%s %s - %s - ($%s)%n",
                        e.getFirstName(), e.getLastName(), e.getJobTitle(), e.getSalary()));
    }
}
