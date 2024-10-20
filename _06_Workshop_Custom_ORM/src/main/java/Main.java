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
        em.doCreate(User.class);
        em.doAlter(User.class);

//        User user1 = new User("user", "pass", 20, LocalDate.now());
        User user2 = new User("user2", "pass2", 22, LocalDate.now());
        user2.setSalary(1500.00);
        user2.setPhoneNumber("+359887123456");

        em.persist(user2); // user2 -> id = 0

        User firstUser = em.findFirst(User.class);
        System.out.println(firstUser);
        em.delete(firstUser);

//        em.delete(User.class, "id = 1");

    }
}
