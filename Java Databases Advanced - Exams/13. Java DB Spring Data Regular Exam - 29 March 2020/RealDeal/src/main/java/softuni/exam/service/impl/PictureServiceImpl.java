package softuni.exam.service.impl;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.json.PictureSeedDto;
import softuni.exam.models.entity.Picture;
import softuni.exam.repository.PictureRepository;
import softuni.exam.service.CarService;
import softuni.exam.service.PictureService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@AllArgsConstructor
@Service
public class PictureServiceImpl implements PictureService {

    private static final String FILE_PATH = "src/main/resources/files/json/pictures.json";

    private final PictureRepository pictureRepository;
    private final Gson gson;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final CarService carService;

    @Override
    public boolean areImported() {
        return this.pictureRepository.count() > 0;
    }

    @Override
    public String readPicturesFromFile() throws IOException {
        return Files.readString(Path.of(FILE_PATH));
    }

    @Override
    public String importPictures() throws IOException {
        StringBuilder sb = new StringBuilder();

        PictureSeedDto[] pictureSeedDtos = this.gson.fromJson(readPicturesFromFile(), PictureSeedDto[].class);

        for (PictureSeedDto pictureSeedDto : pictureSeedDtos) {
            Optional<Picture> pictureByName = this.pictureRepository.findPictureByName(pictureSeedDto.getName());

            if (this.validationUtil.isValid(pictureSeedDto) && pictureByName.isEmpty()) {
                Picture picture = this.modelMapper.map(pictureSeedDto, Picture.class);
                picture.setCar(this.carService.findById(pictureSeedDto.getCar()));
                this.pictureRepository.save(picture);
                sb.append(String.format("Successfully import picture - %s", pictureSeedDto.getName()));
            } else {
                sb.append("Invalid picture");
            }

            sb.append(System.lineSeparator());
        }

        return sb.toString().trim();
    }
}
