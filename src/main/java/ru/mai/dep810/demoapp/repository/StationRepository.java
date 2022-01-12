package ru.mai.dep810.demoapp.repository;

import java.util.List;
import ru.mai.dep810.demoapp.model.Station;

public interface StationRepository {

    List<Station> findAll();

    Station findById(String id);

    Station save(Station station);

    void delete(String id);
}
