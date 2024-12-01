package softuni.exam.util;

import jakarta.xml.bind.JAXBException;
import java.io.FileNotFoundException;

public interface XmlParser {

    <T> T convertFromFile(String filePath, Class<T> clazz) throws JAXBException, FileNotFoundException;

}
