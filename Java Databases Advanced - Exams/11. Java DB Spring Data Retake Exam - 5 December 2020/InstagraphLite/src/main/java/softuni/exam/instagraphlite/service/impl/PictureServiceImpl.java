package softuni.exam.instagraphlite.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.instagraphlite.models.dtos.json.PictureSeedDto;
import softuni.exam.instagraphlite.models.entities.Picture;
import softuni.exam.instagraphlite.repository.PictureRepository;
import softuni.exam.instagraphlite.service.PictureService;
import softuni.exam.instagraphlite.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Arrays;
import java.util.Locale;

@Service
public class PictureServiceImpl implements PictureService {

    private static final Locale locale = Locale.US;
    private static final DecimalFormat df = new DecimalFormat("####.00", DecimalFormatSymbols.getInstance(locale));

    private static final String FILE_PATH = "src/main/resources/files/pictures.json";

    private final PictureRepository pictureRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final Gson gson;

    @Autowired
    public PictureServiceImpl(PictureRepository pictureRepository, ModelMapper modelMapper, ValidationUtil validationUtil, Gson gson) {
        this.pictureRepository = pictureRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gson = gson;
    }

    @Override
    public boolean areImported() {
        return this.pictureRepository.count() > 0;
    }

    @Override
    public String readFromFileContent() throws IOException {
        return Files.readString(Path.of(FILE_PATH));
    }

    @Override
    public String importPictures() throws IOException {
        StringBuilder output = new StringBuilder();

        PictureSeedDto[] pictureSeedDtos = this.gson.fromJson(readFromFileContent(), PictureSeedDto[].class);

        Arrays.stream(pictureSeedDtos)
                .filter(pictureSeedDto -> {
                    boolean isValid = this.validationUtil.isValid(pictureSeedDto) && !isExist(pictureSeedDto.getPath());

                    output.append(isValid
                                    ? "Successfully imported Picture, with size " + df.format(pictureSeedDto.getSize())
                                    : "Invalid Picture")
                            .append(System.lineSeparator());

                    return isValid;
                })
                .map(pictureSeedDto -> this.modelMapper.map(pictureSeedDto, Picture.class))
                .forEach(pictureRepository::save);

        return output.toString().trim();
    }

    @Override
    public boolean isExist(String path) {
        return this.pictureRepository.existsByPath(path);
    }

    @Override
    public String exportPictures() {
        StringBuilder output = new StringBuilder();

        this.pictureRepository.findAllBySizeGreaterThanOrderBySizeAsc(30000D)
                .forEach(picture ->
                        output.append(String.format("%s - %s", df.format(picture.getSize()), picture.getPath()))
                                .append(System.lineSeparator()));

        return output.toString().trim();
    }

    @Override
    public Picture findByPath(String path) {
        return this.pictureRepository.findByPath(path).orElse(null);
    }
}
