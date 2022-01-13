package ru.mai.dep810.demoapp.repository;

import java.util.List;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import ru.mai.dep810.demoapp.model.Station;
import ru.mai.dep810.demoapp.model.Ticket;

@Repository
public
class MongoTicketRepository implements TicketRepository {

    public static final String COLLECTION_NAME = "ticket";

    private final MongoTemplate mongoTemplate;

    public MongoTicketRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<Ticket> findAll() {
        return mongoTemplate.findAll(Ticket.class, COLLECTION_NAME);
    }

    @Override
    public Ticket findById(String id) {
        return mongoTemplate.findById(id, Ticket.class, COLLECTION_NAME);
    }

    @Override
    public Ticket save(Ticket ticket) {
        return mongoTemplate.save(ticket, COLLECTION_NAME);
    }

    @Override
    public void delete(String id) {
        Query query = Query.query(Criteria.where("_id").is(id));
        mongoTemplate.remove(query, COLLECTION_NAME);
    }
}
