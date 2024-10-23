import entities.Project;

import java.util.Comparator;

public class _09_FindLatest10Projects {
    public static void main(String[] args) {
        JPA_Util.createEntityManager()
                .createQuery("FROM Project ORDER BY startDate DESC, name", Project.class)
                .setMaxResults(10)
                .getResultList()
                .stream()
                .sorted(Comparator.comparing(Project::getName))
                .forEach(Project::printGeneralInformation);
    }
}
