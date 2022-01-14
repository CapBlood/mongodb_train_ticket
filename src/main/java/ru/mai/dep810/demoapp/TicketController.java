package ru.mai.dep810.demoapp;

import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import ru.mai.dep810.demoapp.model.Ticket;
import ru.mai.dep810.demoapp.repository.HazelcastCachedTicketRepository;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;


@RestController
public class TicketController {

    private final HazelcastCachedTicketRepository hazelcastCachedTicketRepository;

    public TicketController(@Qualifier("hazelcastCachedTicketRepository") HazelcastCachedTicketRepository hazelcastCachedTicketRepository) {
        this.hazelcastCachedTicketRepository = hazelcastCachedTicketRepository;
    }

    @ApiOperation("Получение списка билетов по id")
    @GetMapping("/ticket")
    public List<Ticket> searchTickets(@RequestParam("id") @Parameter(description = "id поезда для поиска билетов") String id) {
        return hazelcastCachedTicketRepository.findFree(id);
    }

    @GetMapping("/ticket/{id}")
    @ApiOperation("Покупка билета по id")
    public Ticket getTicketById(@PathVariable("id") String id) {
        return hazelcastCachedTicketRepository.findById(id);
    }

    @GetMapping("/ticket/pay/{id}")
    @ApiOperation("Покупка билета по id")
    public void pay(@PathVariable("id") @Parameter(description = "id билета для покупки") String id) {
        hazelcastCachedTicketRepository.pay(id);
    }

    @PostMapping("/ticket")
    @ApiIgnore
    public Ticket addTicket(@RequestBody Ticket ticket) {
//        return hazelcastCachedTicketRepository.addToIndex(ticket);
        return hazelcastCachedTicketRepository.save(ticket);
    }

    @PutMapping("/ticket/{id}")
    @ApiIgnore
    public Ticket updateTrain(@PathVariable("id")String id, @RequestBody Ticket ticket) {
        Ticket existing = hazelcastCachedTicketRepository.findById(id);

        existing.setId(ticket.getId());
        return hazelcastCachedTicketRepository.save(existing);
    }

    @DeleteMapping("/ticket/{id}")
    @ApiIgnore
    public void deleteTrain(@PathVariable("id") String id) {
        hazelcastCachedTicketRepository.delete(id);
    }
}
