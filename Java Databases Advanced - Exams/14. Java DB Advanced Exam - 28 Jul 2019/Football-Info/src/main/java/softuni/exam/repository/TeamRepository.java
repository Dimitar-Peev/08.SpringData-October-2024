package softuni.exam.repository;

import org.hibernate.validator.constraints.Length;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.exam.domain.entities.Team;

@Repository
public interface TeamRepository extends JpaRepository<Team, Integer> {

    Team findByName(String name);

    boolean existsTeamByName(@Length(min = 3, max = 20, message = "Name must be between 3 and 20 characters.") String name);
}
