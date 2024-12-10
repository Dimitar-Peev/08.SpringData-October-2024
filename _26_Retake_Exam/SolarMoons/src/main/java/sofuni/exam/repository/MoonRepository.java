package sofuni.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sofuni.exam.models.entity.Moon;
import sofuni.exam.models.enums.Type;

import java.util.List;

@Repository
public interface MoonRepository extends JpaRepository<Moon, Long> {

    boolean existsByName(String name);

    List<Moon> findMoonByPlanetTypeAndRadiusGreaterThanEqualAndRadiusLessThanEqualOrderByName(Type planet_type, Double radius, Double radius2);
}
