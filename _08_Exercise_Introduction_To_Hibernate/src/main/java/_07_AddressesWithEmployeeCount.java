import entities.Address;

public class _07_AddressesWithEmployeeCount {
    public static void main(String[] args) {
        JPA_Util.createEntityManager()
                .createQuery("FROM Address ORDER BY employees.size DESC", Address.class)
                .setMaxResults(10)
                .getResultList()
                .forEach(Address::printGeneralInformation);
    }
}
