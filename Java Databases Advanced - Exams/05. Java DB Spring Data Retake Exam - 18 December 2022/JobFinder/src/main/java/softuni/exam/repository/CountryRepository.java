package softuni.exam.repository;

import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.exam.models.entity.Country;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {

    boolean existsByName(@Size(min = 2, max = 30) String name);

    Country findCountryById(long id);
}
