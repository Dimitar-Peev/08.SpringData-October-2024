package softuni.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.exam.models.entity.Mechanic;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@Repository
public interface MechanicRepository extends JpaRepository<Mechanic, Long> {

    Optional<Mechanic> findFirstByFirstName(String firstName);

    boolean existsByEmail(@NotNull @Email String email);
}
