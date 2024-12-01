package softuni.exam.repository;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import softuni.exam.models.entity.Attraction;

import java.util.List;

@Repository
public interface AttractionRepository extends JpaRepository<Attraction, Long> {
    boolean existsAttractionByName(@Size(min = 5, max = 40) @NotNull String name);

    Attraction findById(long id);

    @Query("SELECT a FROM Attraction a " +
            "WHERE (a.type = 'historical site' OR a.type = 'archaeological site') " +
            "AND a.elevation >= 300 " +
            "ORDER BY a.name ASC, a.country.name ASC")
    List<Attraction> findHistoricalAndArchaeologicalSitesWithElevationAbove300();
}
