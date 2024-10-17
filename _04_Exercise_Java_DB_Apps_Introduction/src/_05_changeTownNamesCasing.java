import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class _05_changeTownNamesCasing {
    private static final Connection connection = DBConnection.getConnection();
    private static final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws SQLException, IOException {
        System.out.println("Enter country name:");
        String country = reader.readLine();

        PreparedStatement preparedStatement = connection.prepareStatement(
                "UPDATE towns SET name = UPPER(name) WHERE country = ?");
        preparedStatement.setString(1, country);

        int changedTowns = preparedStatement.executeUpdate();

        if (changedTowns == 0) {
            System.out.println("No town names were affected.");
        } else {
            System.out.printf("%d town names were affected.%n", changedTowns);

            preparedStatement = connection.prepareStatement("SELECT name FROM towns WHERE country = ?");
            preparedStatement.setString(1, country);

            ResultSet resultSet = preparedStatement.executeQuery();

            List<String> towns = new ArrayList<>();
            while (resultSet.next()) {
                towns.add(resultSet.getString("name"));
            }
            System.out.println(towns);
        }

        connection.close();
    }
}
