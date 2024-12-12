package softuni.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.exam.models.entity.Car;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

    boolean existsByPlateNumber(@NotNull @Size(min = 2, max = 30) String plateNumber);
}
