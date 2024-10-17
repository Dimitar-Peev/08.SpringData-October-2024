import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;

public class _09_increaseAgeStoredProcedure {
    private static final Connection connection = DBConnection.getConnection();
    private static final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws SQLException, IOException {
        System.out.println("Enter minion_id:");
        int minionId = Integer.parseInt(reader.readLine());

        CallableStatement callableStatement = connection.prepareCall("CALL usp_get_older(?)");
        callableStatement.setInt(1, minionId);
        callableStatement.execute();

        String query = "SELECT name, age FROM minions WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, minionId);

        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();

        System.out.printf("%s %d%n", resultSet.getString("name"), resultSet.getInt("age"));

        connection.close();
    }
}
