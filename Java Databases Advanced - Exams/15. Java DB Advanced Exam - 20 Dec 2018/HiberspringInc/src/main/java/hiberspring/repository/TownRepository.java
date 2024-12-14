package hiberspring.repository;

import hiberspring.domain.entities.Town;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TownRepository extends JpaRepository<Town, Long> {

    Town findByName(String name);

    boolean existsByName(@NotNull String townName);
}
