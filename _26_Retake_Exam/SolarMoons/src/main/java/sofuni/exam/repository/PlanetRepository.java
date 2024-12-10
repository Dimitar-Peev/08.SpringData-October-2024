package sofuni.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sofuni.exam.models.entity.Planet;

@Repository
public interface PlanetRepository extends JpaRepository<Planet, Long> {

    boolean existsByName(String name);

    Planet findPlanetById(Long id);
}
