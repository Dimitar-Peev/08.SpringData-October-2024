package softuni.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import softuni.exam.models.entity.Device;
import softuni.exam.models.entity.DeviceType;

import java.util.List;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {

    boolean existsDeviceByBrandAndAndModel(String brand, String model);

    @Query("SELECT d FROM Device d " +
            "WHERE d.deviceType = :type " +
            "AND d.price < :price " +
            "AND d.storage >= :storage " +
            "ORDER BY LOWER(d.brand) ASC")
    List<Device> findSmartPhonesForExport(DeviceType type, Double price, Integer storage);
}
