import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;

public class _06_removeVillain {
    private static final Connection connection = DBConnection.getConnection();
    private static final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws SQLException, IOException {
        System.out.println("Enter villain id:");
        int villainId = Integer.parseInt(reader.readLine());

        PreparedStatement preparedStatement = connection.prepareStatement("SELECT name FROM villains WHERE id = ?");
        preparedStatement.setInt(1, villainId);

        ResultSet resultSet = preparedStatement.executeQuery();

        if (!resultSet.next()) {
            System.out.println("No such villain was found");
            return;
        }

        String villainName = resultSet.getString("name");

        preparedStatement = connection.prepareStatement("DELETE FROM minions_villains WHERE villain_id = ?");
        preparedStatement.setInt(1, villainId);
        int deletedCount = preparedStatement.executeUpdate();

        preparedStatement = connection.prepareStatement("DELETE FROM villains WHERE id = ?");
        preparedStatement.setInt(1, villainId);
        preparedStatement.executeUpdate();

        System.out.println(villainName + " was deleted");
        System.out.println(deletedCount + " minions released");

        connection.close();
    }
}
