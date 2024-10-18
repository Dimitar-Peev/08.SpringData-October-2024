import entities.User;
import orm.config.MyConnector;
import orm.context.EntityManager;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws SQLException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your password:");
        String password = scanner.nextLine();

        MyConnector.createConnection("root", password, "mini_orm");

        Connection connection = MyConnector.getConnection();
        EntityManager<User> em = new EntityManager<>(connection);

        User user1 = new User("user", "pass", 20, LocalDate.now());
        User user2 = new User("user2", "pass2", 22, LocalDate.now());
        user2.setId(2);

//        int idValue = em.getIdValue(user1); // 0
//        int idValue1 = em.getIdValue(user2); // 2

//        em.persist(user1); // insert

        User firstUser = em.findFirst(User.class);

        em.persist(user2); // update

        firstUser.setUsername("updated");
        em.persist(firstUser);
        System.out.println();

        Iterable<User> users = em.find(User.class);

        for (User user : users) {
            System.out.println(user);
        }
        System.out.println();
    }
}
