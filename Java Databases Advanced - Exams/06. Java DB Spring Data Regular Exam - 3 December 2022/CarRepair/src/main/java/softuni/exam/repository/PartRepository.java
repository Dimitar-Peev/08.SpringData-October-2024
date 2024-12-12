package softuni.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.exam.models.entity.Part;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Repository
public interface PartRepository extends JpaRepository<Part, Long> {

    boolean existsByPartName(@NotNull @Size(min = 2, max = 19) String partName);
}
