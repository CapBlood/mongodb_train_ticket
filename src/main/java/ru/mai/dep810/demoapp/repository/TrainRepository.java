package ru.mai.dep810.demoapp.repository;

import java.util.List;
import ru.mai.dep810.demoapp.model.Train;

public interface TrainRepository {
    Train findById(String id);

    Train save(Train student);

    void delete(String id);
}
