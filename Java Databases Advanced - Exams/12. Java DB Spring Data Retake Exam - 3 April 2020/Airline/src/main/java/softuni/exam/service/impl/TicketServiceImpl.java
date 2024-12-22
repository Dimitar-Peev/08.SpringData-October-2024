package softuni.exam.service.impl;

import jakarta.xml.bind.JAXBException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dtos.xml.TicketSeedDto;
import softuni.exam.models.dtos.xml.TicketSeedRootDto;
import softuni.exam.models.entities.Ticket;
import softuni.exam.repository.PassengerRepository;
import softuni.exam.repository.PlaneRepository;
import softuni.exam.repository.TicketRepository;
import softuni.exam.repository.TownRepository;
import softuni.exam.service.TicketService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class TicketServiceImpl implements TicketService {

    private static final String FILE_PATH = "src/main/resources/files/xml/tickets.xml";

    private final TicketRepository ticketRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final XmlParser xmlParser;
    private final TownRepository townRepository;
    private final PassengerRepository passengerRepository;
    private final PlaneRepository planeRepository;

    @Autowired
    public TicketServiceImpl(TicketRepository ticketRepository, ModelMapper modelMapper, ValidationUtil validationUtil,
                             XmlParser xmlParser, TownRepository townRepository, PassengerRepository passengerRepository, PlaneRepository planeRepository) {
        this.ticketRepository = ticketRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.xmlParser = xmlParser;
        this.townRepository = townRepository;
        this.passengerRepository = passengerRepository;
        this.planeRepository = planeRepository;
    }

    @Override
    public boolean areImported() {
        return this.ticketRepository.count() > 0;
    }

    @Override
    public String readTicketsFileContent() throws IOException {
        return Files.readString(Path.of(FILE_PATH));
    }

    @Override
    public String importTickets() throws JAXBException, FileNotFoundException {
        StringBuilder output = new StringBuilder();

        TicketSeedRootDto ticketSeedRootDto = this.xmlParser.convertFromFile(FILE_PATH, TicketSeedRootDto.class);

        ticketSeedRootDto
                .getTickets()
                .stream()
                .filter(ticketSeedDto -> {
                    boolean isValid = this.validationUtil.isValid(ticketSeedDto);

                    output.append(isValid ? String.format("Successfully imported Ticket %s - %s",
                                    ticketSeedDto.getFromTown().getName(), ticketSeedDto.getToTown().getName())
                                    : "Invalid Ticket")
                            .append(System.lineSeparator());

                    return isValid;
                })
                .map(this::newTicket)
                .forEach(ticketRepository::save);

        return output.toString().trim();
    }

    private Ticket newTicket(TicketSeedDto ticketSeedDto) {
        Ticket ticket = this.modelMapper.map(ticketSeedDto, Ticket.class);

        LocalDateTime localDateTime = LocalDateTime.parse(ticketSeedDto.getTakeOff(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        ticket.setTakeoff(localDateTime);
        ticket.setFromTown(this.townRepository.findByName(ticketSeedDto.getFromTown().getName()));
        ticket.setToTown(this.townRepository.findByName(ticketSeedDto.getToTown().getName()));
        ticket.setPassenger(this.passengerRepository.findByEmail(ticketSeedDto.getPassenger().getEmail()));
        ticket.setPlane(this.planeRepository.findByRegisterNumber(ticketSeedDto.getPlane().getPlaneNumber()));

        return ticket;
    }
}
