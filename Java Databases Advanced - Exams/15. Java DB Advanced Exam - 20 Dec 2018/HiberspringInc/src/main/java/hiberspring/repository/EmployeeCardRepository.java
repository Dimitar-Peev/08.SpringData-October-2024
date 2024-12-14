package hiberspring.repository;

import hiberspring.domain.entities.EmployeeCard;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeCardRepository extends JpaRepository<EmployeeCard, Long> {

   EmployeeCard findByNumber(String cardNumber);

   boolean existsByNumber(@NotNull String number);
}
