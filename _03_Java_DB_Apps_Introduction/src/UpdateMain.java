import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class UpdateMain {
    public static void main(String[] args) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        String user = "root";
        System.out.println("Enter your password:");
        String pass = scanner.nextLine();

        Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/soft_uni", user, pass);

        PreparedStatement preparedStatement = connection.prepareStatement(
            "UPDATE employees SET salary = salary * 2 WHERE employee_id = 1;");

        int resultSet = preparedStatement.executeUpdate();

        System.out.println(resultSet + " row(s) affected");
    }
}
