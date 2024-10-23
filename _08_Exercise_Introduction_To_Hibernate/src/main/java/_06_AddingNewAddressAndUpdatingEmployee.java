import entities.Address;

import javax.persistence.EntityManager;
import java.util.Scanner;

public class _06_AddingNewAddressAndUpdatingEmployee {
    public static void main(String[] args) {
        EntityManager entityManager = HibernateUtil.createEntityManager();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the last name: ");
        String lastName = scanner.nextLine();

        entityManager.getTransaction().begin();
        String addressText = "Vitoshka 15";

        Address address = new Address();
        address.setText(addressText);
        entityManager.persist(address);

        entityManager.createQuery("UPDATE Employee e SET e.address = :address WHERE e.lastName = :name")
                .setParameter("address", address)
                .setParameter("name", lastName)
                .executeUpdate();

        entityManager.getTransaction().commit();
        entityManager.close();
    }
}
