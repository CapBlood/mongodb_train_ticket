package ru.mai.dep810.demoapp;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
//import ru.mai.dep810.demoapp.repository.PostElasticRepository;
import ru.mai.dep810.demoapp.model.Ticket;
import ru.mai.dep810.demoapp.model.Train;
import ru.mai.dep810.demoapp.repository.TicketElasticRepository;
import ru.mai.dep810.demoapp.repository.TrainElasticRepository;

@RestController
public class TicketController {

    private TicketElasticRepository ticketElasticRepository;

    public TicketController(TicketElasticRepository trainTicketElasticRepository) {
        this.ticketElasticRepository = trainTicketElasticRepository;
    }

    @GetMapping("/ticket")
    public List<Ticket> searchTickets(@RequestParam("id") String id) {
        return ticketElasticRepository.fullTextSearch(id);
    }

    @GetMapping("/ticket/{id}")
    public Ticket getTicketById(@PathVariable("id") String id) {
        return ticketElasticRepository.findById(id);
    }

    @PostMapping("/ticket")
    public Ticket addTicket(@RequestBody Ticket ticket) {
        return ticketElasticRepository.addToIndex(ticket);
    }
}
