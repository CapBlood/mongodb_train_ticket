package ru.mai.dep810.demoapp.repository;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import ru.mai.dep810.demoapp.model.Station;
import ru.mai.dep810.demoapp.model.Ticket;

@Repository
public class HazelcastCachedTicketRepository implements TicketRepository  {
    private final TicketElasticRepository delegate;
    private final TicketRepository delegateMongo;
    private final IMap<String, Ticket> cache;

    public HazelcastCachedTicketRepository(@Qualifier("ticketElasticRepository") TicketElasticRepository delegate,
                                           @Qualifier("mongoTicketRepository") TicketRepository delegateMongo,
                                           @Autowired HazelcastInstance hazelcastInstance) {
        this.cache = hazelcastInstance.getMap("ticket");
        this.delegate = delegate;
        this.delegateMongo = delegateMongo;
    }

    public List<Ticket> findFree(String id) {
        return delegate.fullTextSearch(id);
//        return
    }

    @Override
    public List<Ticket> findAll() {
        return delegateMongo.findAll();
    }

    @Override
    public Ticket findById(String id) {
        return cache.computeIfAbsent(id, delegate::findById);
    }

    @Override
    public Ticket save(Ticket ticket) {
        cache.lock(ticket.getId());
        try {
            Ticket s = delegateMongo.save(ticket);
//            delegate.addToIndex(ticket);
            cache.put(s.getId(), s);
//            cache.remove(ticket.getId());
            return s;
        } finally {
            cache.unlock(ticket.getId());
        }
    }

    public void pay(String id) {
        Ticket ticket = delegateMongo.findById(id);

        cache.lock(ticket.getId());
        try {
            ticket.setBought(true);
            System.out.println(ticket.getBought());
            Ticket s = delegateMongo.save(ticket);
            delegate.updateIndex(s);
            cache.put(s.getId(), s);
//            cache.remove(ticket.getId());
        } finally {
            cache.unlock(ticket.getId());
        }
    }

    @Override
    public void delete(String id) {
        delegateMongo.delete(id);
        cache.remove(id);
    }
}
