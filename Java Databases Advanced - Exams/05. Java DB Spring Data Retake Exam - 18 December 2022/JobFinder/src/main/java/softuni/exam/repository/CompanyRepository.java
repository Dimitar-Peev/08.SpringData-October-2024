package softuni.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.exam.models.entity.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    Company findById(long id);

    boolean existsByName(String companyName);
}
