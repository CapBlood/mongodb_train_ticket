package ru.mai.dep810.demoapp;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.mai.dep810.demoapp.model.Station;
//import ru.mai.dep810.demoapp.repository.PostElasticRepository;
import ru.mai.dep810.demoapp.model.Ticket;
import ru.mai.dep810.demoapp.repository.TrainTicketElasticRepository;

@RestController
public class TrainTicketController {

    private TrainTicketElasticRepository trainTicketElasticRepository;

    public TrainTicketController(TrainTicketElasticRepository trainTicketElasticRepository) {
        this.trainTicketElasticRepository = trainTicketElasticRepository;
    }

    @GetMapping("/ticket")
    public List<Ticket> searchTickets(@RequestParam("q") String query) {
        return trainTicketElasticRepository.fullTextSearch(query);
    }

    @GetMapping("/ticket/{id}")
    public Ticket getTicketById(@PathVariable("id") String id) {
        return trainTicketElasticRepository.findById(id);
    }

    @PostMapping("/ticket")
    public Ticket addTicket(@RequestBody Ticket ticket) {
        return trainTicketElasticRepository.addToIndex(ticket);
    }
}
