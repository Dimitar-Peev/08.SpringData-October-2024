package softuni.exam.instagraphlite.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.exam.instagraphlite.models.dtos.ExportPictureDto;
import softuni.exam.instagraphlite.models.entities.Picture;

import java.util.List;
import java.util.Optional;

@Repository
public interface PictureRepository extends JpaRepository<Picture, Long> {

    Optional<Picture> findByPath(String path);

    boolean existsByPath(String path);

    List<ExportPictureDto> findAllBySizeGreaterThanOrderBySizeAsc(Double size);
}
