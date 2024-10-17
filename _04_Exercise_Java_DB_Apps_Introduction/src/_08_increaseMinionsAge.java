import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.Arrays;

public class _08_increaseMinionsAge {
    private static final Connection connection = DBConnection.getConnection();
    private static final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws SQLException, IOException {
        System.out.println("Enter minion IDs, separated by space: ");
        int[] inputArray = Arrays.stream(reader.readLine().split("\\s+")).mapToInt(Integer::parseInt).toArray();

        for (int i = 0; i < Arrays.stream(inputArray).count(); i++) {
            String query = "UPDATE minions SET name = LOWER(name), age = age + 1 WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, inputArray[i]);
            preparedStatement.executeUpdate();
        }

        PreparedStatement preparedStatement = connection.prepareStatement("SELECT name, age FROM minions");

        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            System.out.printf("%s %d%n", resultSet.getString("name"), resultSet.getInt("age"));
        }

        connection.close();
    }
}
