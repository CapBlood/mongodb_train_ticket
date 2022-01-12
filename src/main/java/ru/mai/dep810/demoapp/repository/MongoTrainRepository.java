package ru.mai.dep810.demoapp.repository;

import java.util.List;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import ru.mai.dep810.demoapp.model.Train;

@Repository
class MongoTrainRepository implements TrainRepository {

    public static final String COLLECTION_NAME = "train";

    private final MongoTemplate mongoTemplate;

    public MongoTrainRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Train findById(String id) {
        return mongoTemplate.findById(id, Train.class, COLLECTION_NAME);
    }

    @Override
    public Train save(Train train) {
        return mongoTemplate.save(train, COLLECTION_NAME);
    }

    @Override
    public void delete(String id) {
        Query query = Query.query(Criteria.where("_id").is(id));
        mongoTemplate.remove(query, COLLECTION_NAME);
    }
}
