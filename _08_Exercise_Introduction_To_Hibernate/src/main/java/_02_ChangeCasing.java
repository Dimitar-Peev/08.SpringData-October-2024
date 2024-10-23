import entities.Town;
import javax.persistence.EntityManager;
import java.util.List;

public class _02_ChangeCasing {
    public static void main(String[] args) {
        EntityManager entityManager = HibernateUtil.createEntityManager();

        entityManager.getTransaction().begin();

        // solution 1
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

        // solution 2
//        List<Town> towns1 = entityManager.createQuery("FROM Town WHERE LENGTH(name) <= 5", Town.class).getResultList();
//        towns1.forEach(town -> {
//            town.setName(town.getName().toUpperCase());
//            entityManager.persist(town);
//        });

        // solution 3
//        entityManager.createQuery("UPDATE Town SET name = UPPER(name) WHERE LENGTH(name) <= 5").executeUpdate();

        entityManager.getTransaction().commit();
        entityManager.close();
    }
}
