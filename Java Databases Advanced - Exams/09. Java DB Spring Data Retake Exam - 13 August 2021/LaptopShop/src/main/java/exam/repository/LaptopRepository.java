package exam.repository;

import exam.model.entities.Laptop;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LaptopRepository extends JpaRepository<Laptop, Long> {

    boolean existsByMacAddress(@Size(min = 2) @NotNull String macAddress);

    List<Laptop> findAllByOrderByCpuSpeedDescRamDescStorageDescMacAddressAsc();
}
