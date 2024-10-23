import entities.Employee;

public class _04_EmployeesWithSalaryOver50000 {
    public static void main(String[] args) {
        HibernateUtil.createEntityManager()
                .createQuery("FROM Employee WHERE salary > 50000", Employee.class)
                .getResultList()
                .forEach(employee -> System.out.println(employee.getFirstName()));
    }
}
