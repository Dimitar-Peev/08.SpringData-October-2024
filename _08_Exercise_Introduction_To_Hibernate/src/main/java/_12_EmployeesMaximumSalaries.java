public class _12_EmployeesMaximumSalaries {
    public static void main(String[] args) {
        JPA_Util.createEntityManager()
                .createQuery("SELECT department.name, MAX(salary)" +
                        " FROM Employee " +
                        " GROUP BY department.name" +
                        " HAVING MAX(salary) NOT BETWEEN 30000 AND 70000", Object[].class)
                .getResultList()
                .forEach(objects -> System.out.println(objects[0] + " " + objects[1]));
    }
}