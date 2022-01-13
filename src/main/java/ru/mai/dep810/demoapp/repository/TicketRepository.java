package ru.mai.dep810.demoapp.repository;

import java.util.List;
import ru.mai.dep810.demoapp.model.Station;
import ru.mai.dep810.demoapp.model.Ticket;

public interface TicketRepository {

    List<Ticket> findAll();

    Ticket findById(String id);

    Ticket save(Ticket station);

    void delete(String id);
}
