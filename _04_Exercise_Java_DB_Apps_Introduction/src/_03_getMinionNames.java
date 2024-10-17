import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class _03_getMinionNames {
    private static final Connection connection = DBConnection.getConnection();
    private static final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws SQLException, IOException {
        System.out.println("Enter villain id:");
        int villainId = Integer.parseInt(reader.readLine());

        PreparedStatement preparedStatement = connection.prepareStatement("SELECT name FROM villains WHERE id = ?");
        preparedStatement.setInt(1, villainId);

        ResultSet resultSet = preparedStatement.executeQuery();
        if (!resultSet.next()) {
            System.out.printf("No villain with ID %d exists in the database.%n", villainId);
            return;
        }

        System.out.printf("Villain: %s%n", resultSet.getString("name"));

        preparedStatement = connection.prepareStatement(
                "SELECT m.name, m.age FROM minions m " +
                        "JOIN minions_villains mv on m.id = mv.minion_id " +
                        "WHERE mv.villain_id = ?");
        preparedStatement.setInt(1, villainId);

        resultSet = preparedStatement.executeQuery();

        int count = 1;
        while (resultSet.next()) {
            System.out.printf("%d. %s %d%n", count++,
                    resultSet.getString("name"), resultSet.getInt("age"));
        }

        connection.close();
    }
}
