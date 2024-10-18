import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        String host = "127.0.0.1"; // "localhost";
        String port = "3306";
        String databaseName = "soft_uni";
        String user = "root";
        System.out.println("Enter your password:");
        String pass = scanner.nextLine();

        String url = String.format("jdbc:mysql://%s:%s/%s", host, port, databaseName);

        Connection connection = DriverManager.getConnection(url, user, pass);

        String query = "SELECT COUNT(*) FROM employees";

        PreparedStatement preparedStatement = connection.prepareStatement(query);

        ResultSet resultSet = preparedStatement.executeQuery();

        resultSet.next();
        int employeeCount = resultSet.getInt(1);
        System.out.println("Employee count: " + employeeCount);

        PreparedStatement manyColumns =
                connection.prepareStatement(
                        "SELECT employee_id, first_name, salary " +
                                "FROM employees LIMIT 1");
        ResultSet manyColumnsResult = manyColumns.executeQuery();

        manyColumnsResult.next();
        int id = manyColumnsResult.getInt(1);
        String name = manyColumnsResult.getString(2);
        float salary = manyColumnsResult.getFloat(3);

        System.out.printf("%d | %s | %.2f%n", id, name, salary);

        PreparedStatement manyRows =
                connection.prepareStatement(
                        "SELECT employee_id AS id, first_name, salary " +
                                "FROM employees LIMIT 10");
        ResultSet manyRowsResult = manyRows.executeQuery();

        while (manyRowsResult.next()) {
            System.out.printf("%3d | %-10.10s | %10.2f%n",
                    manyRowsResult.getInt("id"),
                    manyRowsResult.getString("first_name"),
                    manyRowsResult.getFloat("salary")
            );
        }

        PreparedStatement emptyQuery =
                connection.prepareStatement("SELECT * FROM employees WHERE salary < -10");
        ResultSet emptyQueryResult = emptyQuery.executeQuery();

        boolean nextResult = emptyQueryResult.next();
        System.out.println(nextResult); // false
        emptyQueryResult.getInt(1); // Illegal operation on empty result set.
    }
}
