package ru.mai.dep810.demoapp.repository;

import java.util.List;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import ru.mai.dep810.demoapp.model.Station;

@Repository
public
class MongoStationRepository implements StationRepository {

    public static final String COLLECTION_NAME = "station";

    private final MongoTemplate mongoTemplate;

    public MongoStationRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<Station> findAll() {
        return mongoTemplate.findAll(Station.class, COLLECTION_NAME);
    }

    @Override
    public Station findById(String id) {
        return mongoTemplate.findById(id, Station.class, COLLECTION_NAME);
    }

    @Override
    public Station save(Station station) {
        return mongoTemplate.save(station, COLLECTION_NAME);
    }

    @Override
    public void delete(String id) {
        Query query = Query.query(Criteria.where("_id").is(id));
        mongoTemplate.remove(query, COLLECTION_NAME);
    }
}
