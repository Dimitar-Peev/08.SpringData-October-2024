package softuni.exam.service.impl;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.xml.DeviceImportDTO;
import softuni.exam.models.dto.xml.DeviceImportRootDTO;
import softuni.exam.models.entity.Device;
import softuni.exam.models.entity.DeviceType;
import softuni.exam.repository.DeviceRepository;
import softuni.exam.repository.SaleRepository;
import softuni.exam.service.DeviceService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
public class DeviceServiceImpl implements DeviceService {

    private static final String FILE_PATH = "src/main/resources/files/xml/devices.xml";

    private final DeviceRepository deviceRepository;
    private final SaleRepository saleRepository;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;
    private final XmlMapper xmlMapper;

    public DeviceServiceImpl(DeviceRepository deviceRepository, SaleRepository saleRepository,
                             ValidationUtil validationUtil, ModelMapper modelMapper, XmlMapper xmlMapper) {
        this.deviceRepository = deviceRepository;
        this.saleRepository = saleRepository;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.xmlMapper = xmlMapper;
    }

    @Override
    public boolean areImported() {
        return this.deviceRepository.count() > 0;
    }

    @Override
    public String readDevicesFromFile() throws IOException {
        return Files.readString(Path.of(FILE_PATH));
    }

    @Override
    public String importDevices() throws IOException {
        DeviceImportRootDTO deviceImportRootDTO =
                this.xmlMapper.readValue(readDevicesFromFile(), DeviceImportRootDTO.class);

        StringBuilder sb = new StringBuilder();
        deviceImportRootDTO
                .getDevices()
                .stream()
                .filter(deviceImportDTO -> {
                    boolean existsDeviceByBrandAndAndModel = this.deviceRepository.
                            existsDeviceByBrandAndAndModel(deviceImportDTO.getBrand(), deviceImportDTO.getModel());

                    boolean isExistSale = this.saleRepository.existsSaleById(deviceImportDTO.getSaleId());

                    boolean isValid = this.validationUtil.isValid(deviceImportDTO)
                            && !existsDeviceByBrandAndAndModel
                            && isExistSale;

                    sb.append(isValid
                                    ? String.format("Successfully imported device of type %s with brand %s",
                                    deviceImportDTO.getDeviceType(), deviceImportDTO.getBrand())
                                    : "Invalid device")
                            .append(System.lineSeparator());

                    return isValid;
                })
                .map(this::newDevice)
                .forEach(deviceRepository::save);

        return sb.toString().trim();
    }

    private Device newDevice(DeviceImportDTO deviceImportDTO) {
        Device device = modelMapper.map(deviceImportDTO, Device.class);
        DeviceType deviceType = DeviceType.valueOf(deviceImportDTO.getDeviceType());
        device.setDeviceType(deviceType);
        device.setSale(this.saleRepository.findById(deviceImportDTO.getSaleId()).get());
        return device;
    }

    @Override
    public String exportDevices() {
        StringBuilder sb = new StringBuilder();

        List<Device> devices =
                this.deviceRepository.findSmartPhonesForExport(DeviceType.SMART_PHONE, 1000D, 128);

        for (Device device : devices) {
            sb.append(device.toString()).append(System.lineSeparator());
        }

        return sb.toString().trim();
    }
}
