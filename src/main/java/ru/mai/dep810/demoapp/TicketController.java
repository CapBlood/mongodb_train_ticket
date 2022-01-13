package ru.mai.dep810.demoapp;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
//import ru.mai.dep810.demoapp.repository.PostElasticRepository;
import ru.mai.dep810.demoapp.model.Station;
import ru.mai.dep810.demoapp.model.Ticket;
import ru.mai.dep810.demoapp.model.Train;
import ru.mai.dep810.demoapp.repository.HazelcastCachedTicketRepository;
import ru.mai.dep810.demoapp.repository.StationRepository;
import ru.mai.dep810.demoapp.repository.TicketElasticRepository;
import ru.mai.dep810.demoapp.repository.TrainElasticRepository;

@RestController
public class TicketController {

    private HazelcastCachedTicketRepository hazelcastCachedTicketRepository;

    public TicketController(@Qualifier("hazelcastCachedTicketRepository") HazelcastCachedTicketRepository hazelcastCachedTicketRepository) {
        this.hazelcastCachedTicketRepository = hazelcastCachedTicketRepository;
    }

    @GetMapping("/ticket")
    public List<Ticket> searchTickets(@RequestParam("id") String id) {
        return hazelcastCachedTicketRepository.findFree(id);
    }

    @GetMapping("/ticket/{id}")
    public Ticket getTicketById(@PathVariable("id") String id) {
        return hazelcastCachedTicketRepository.findById(id);
    }

    @GetMapping("/ticket/pay/{id}")
    public void pay(@PathVariable("id") String id) {
        hazelcastCachedTicketRepository.pay(id);
    }

    @PostMapping("/ticket")
    public Ticket addTicket(@RequestBody Ticket ticket) {
//        return hazelcastCachedTicketRepository.addToIndex(ticket);
        return hazelcastCachedTicketRepository.save(ticket);
    }

    @PutMapping("/ticket/{id}")
    public Ticket updateTrain(@PathVariable("id")String id, @RequestBody Ticket ticket) {
        Ticket existing = hazelcastCachedTicketRepository.findById(id);

        existing.setId(ticket.getId());
        return hazelcastCachedTicketRepository.save(existing);
    }

    @DeleteMapping("/ticket/{id}")
    public void deleteTrain(@PathVariable("id") String id) {
        hazelcastCachedTicketRepository.delete(id);
    }
}
