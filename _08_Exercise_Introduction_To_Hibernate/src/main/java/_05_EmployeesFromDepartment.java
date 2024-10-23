import entities.Employee;

public class _05_EmployeesFromDepartment {
    public static void main(String[] args) {
        JPA_Util.createEntityManager()
                .createQuery("FROM Employee WHERE department.name = :departmentName ORDER BY salary, id",
                        Employee.class)
                .setParameter("departmentName", "Research and Development")
                .getResultList()
                .forEach(Employee::printFullNameDepartmentNameAndSalary);
    }
}
