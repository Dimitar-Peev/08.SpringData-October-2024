import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayDeque;

public class _07_printAllMinionNames {
    private static final Connection connection = DBConnection.getConnection();

    public static void main(String[] args) throws SQLException {
        ResultSet resultSet = connection.createStatement().executeQuery("SELECT name FROM minions");

        ArrayDeque<String> minionNames = new ArrayDeque<>();
        while (resultSet.next()) {
            minionNames.offer(resultSet.getString("name"));
        }

        while (!minionNames.isEmpty()) {
            System.out.println(minionNames.pop());
            if (!minionNames.isEmpty()) {
                System.out.println(minionNames.pollLast());
            }
        }

        connection.close();
    }
}
