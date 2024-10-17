import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class _02_getVillainsNames {
    private static final Connection connection = DBConnection.getConnection();

    public static void main(String[] args) throws SQLException {
        String query = """
                SELECT v.name, COUNT(*) AS count FROM villains AS v
                JOIN minions_villains mv on v.id = mv.villain_id
                GROUP BY v.name
                HAVING count > 15
                ORDER BY count DESC
                """;

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()) {
            String name = resultSet.getString("name");
            int count = resultSet.getInt("count");
            System.out.println(name + " " + count);
        }

        connection.close();
    }
}
