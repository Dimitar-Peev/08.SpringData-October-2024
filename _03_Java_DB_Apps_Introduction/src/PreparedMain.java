import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class PreparedMain {
    public static void main(String[] args) throws SQLException{
        Scanner scanner = new Scanner(System.in);
        String user = "root";
        System.out.println("Enter your password:");
        String pass = scanner.nextLine();

        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/soft_uni", user, pass);

        String query = "SELECT * FROM employees WHERE first_name LIKE ?";

        PreparedStatement preparedStatement = connection.prepareStatement(query);

        preparedStatement.setString(1, "%gu%");
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            System.out.printf("%d -> %s%n",
                    resultSet.getInt("employee_id"),
                    resultSet.getString("first_name")
            );
        }

        System.out.println("---------------");

        String unsafeQuery = "SELECT * FROM employees WHERE salary > ";
        unsafeQuery += "48000"; // Good case
//        unsafeQuery += "1 OR 1 = 1"; // Bad case
//        unsafeQuery += "1; SELECT * FROM users;"; // Bad case
//        unsafeQuery += "1; DROP TABLE users;"; // Bad case
//      SELECT * FROM users WHERE username = "%s" AND password = "%s";
//        username = pesho"; SELECT * FROM users WHERE role = admin;
        ResultSet unsafeResult = connection.createStatement().executeQuery(unsafeQuery);

        while (unsafeResult.next()) {
            System.out.printf("%d -> %s%n",
                    unsafeResult.getInt("employee_id"),
                    unsafeResult.getString("first_name")
            );
        }
    }
}
