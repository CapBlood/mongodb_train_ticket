package ru.mai.dep810.demoapp;

import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.mai.dep810.demoapp.model.Station;
import ru.mai.dep810.demoapp.repository.StationRepository;

@RestController
public class StationController {

    private StationRepository stationRepository;

    public StationController(@Qualifier("hazelcastCachedStationRepository") StationRepository trainRepository) {
        this.stationRepository = trainRepository;
    }

    @GetMapping("/station")
    public List<Station> getAllTrains() {
        return stationRepository.findAll();
    }

    @GetMapping("/station/{id}")
    public Station getTrainById(@PathVariable("id") String id) {
        return stationRepository.findById(id);
    }

    @PostMapping("/station")
    public Station addTrain(@RequestBody Station train) {
        return stationRepository.save(train);
    }

    @PutMapping("/station/{id}")
    public Station updateTrain(@PathVariable("id")String id, @RequestBody Station station) {
        Station existing = stationRepository.findById(id);

        existing.setId(station.getId());
        return stationRepository.save(existing);
    }

    @DeleteMapping("/station/{id}")
    public void deleteTrain(@PathVariable("id") String id) {
        stationRepository.delete(id);
    }
}
