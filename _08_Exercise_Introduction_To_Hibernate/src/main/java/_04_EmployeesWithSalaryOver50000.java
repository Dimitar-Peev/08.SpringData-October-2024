import entities.Employee;

public class _04_EmployeesWithSalaryOver50000 {
    public static void main(String[] args) {
        JPA_Util.createEntityManager()
                .createQuery("FROM Employee WHERE salary > 50000", Employee.class)
                .getResultList()
                .forEach(employee -> System.out.println(employee.getFirstName()));
    }
}
