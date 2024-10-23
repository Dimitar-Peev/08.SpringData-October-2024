import entities.Town;
import javax.persistence.EntityManager;
import java.util.List;

public class _02_ChangeCasing {
    public static void main(String[] args) {
        EntityManager entityManager = JPA_Util.createEntityManager();

        entityManager.getTransaction().begin();

        List<Town> towns = entityManager.createQuery("FROM Town", Town.class).getResultList();

        for (Town town : towns) {
            if (town.getName().length() > 5) {
                entityManager.detach(town);
            } else {
                town.setName(town.getName().toUpperCase());
            }
        }

        for (Town town : towns) {
            if (entityManager.contains(town)) {
                entityManager.persist(town);
            }
        }

        entityManager.getTransaction().commit();
        entityManager.close();
    }
}
