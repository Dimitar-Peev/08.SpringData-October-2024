package softuni.exam.repository;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.exam.domain.entities.Picture;

@Repository
public interface PictureRepository  extends JpaRepository<Picture, Integer> {

    Picture findByUrl(String url);

    boolean existsPictureByUrl(@NotNull String url);
}
