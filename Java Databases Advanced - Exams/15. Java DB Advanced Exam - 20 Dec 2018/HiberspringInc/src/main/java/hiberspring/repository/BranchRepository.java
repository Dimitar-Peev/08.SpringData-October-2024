package hiberspring.repository;

import hiberspring.domain.entities.Branch;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BranchRepository extends JpaRepository<Branch, Long> {

    Branch findByName(String name);

    boolean existsByName(@NotNull String name);
}
