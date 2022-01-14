package ru.mai.dep810.demoapp.repository;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import ru.mai.dep810.demoapp.model.Station;

@Repository
public class HazelcastCachedStationRepository implements StationRepository {
    private final StationRepository delegate;
    private final IMap<String, Station> cache;

    public HazelcastCachedStationRepository(@Qualifier("mongoStationRepository") StationRepository delegate,
                                            @Qualifier("hazelcastInstance") @Autowired HazelcastInstance hazelcastInstance) {
        this.cache = hazelcastInstance.getMap("station");
        this.delegate = delegate;
    }

    @Override
    public List<Station> findAll() {
        return delegate.findAll();
    }

    @Override
    public Station findById(String id) {
        return cache.computeIfAbsent(id, delegate::findById);
    }

    @Override
    public Station save(Station station) {
        cache.lock(station.getId());
        try {
            Station s = delegate.save(station);
            // cache.put(s.getId(), s);
            cache.remove(station.getId());
            return s;
        } finally {
            cache.unlock(station.getId());
        }
    }

    @Override
    public void delete(String id) {
        delegate.delete(id);
        cache.remove(id);
    }
}
