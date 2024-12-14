package hiberspring.repository;

import hiberspring.domain.entities.Employee;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Employee findByFirstNameAndLastNameAndPosition(String firstName, String lastName, String position);

    @Query("SELECT e FROM Employee e WHERE SIZE(e.branch.products) > 0 " +
            "ORDER BY concat(e.firstName, ' ', e.lastName), " +
            "LENGTH(e.position) DESC")
    List<Employee> findAllByBranchWithMoreThanOneProduct();

    boolean existsByCardNumber(@NotNull String card);
}

