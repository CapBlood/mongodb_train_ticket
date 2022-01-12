package ru.mai.dep810.demoapp;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.mai.dep810.demoapp.model.Route;
import ru.mai.dep810.demoapp.model.Train;
import ru.mai.dep810.demoapp.repository.TrainRepository;

@RestController
public class TrainController {

    private TrainRepository trainRepository;

    public TrainController(@Qualifier("hazelcastCachedTrainRepository") TrainRepository trainRepository) {
        this.trainRepository = trainRepository;
    }

//    private final List<Ticket> students = new ArrayList<>(List.of(
//            new Ticket("1", "John Smith", 21, List.of(new Route("RU", "Moscow", "1st Volokolamkiy pr"))),
//            new Ticket("2", "Steve Howking", 25, List.of(new Route("UK", "London", "Liverpool st")))
//    ));

//    @GetMapping("/student")
//    public List<Ticket> getAllStudents() {
//        return studentRepository.findAll();
//    }

    @GetMapping("/train/{id}")
    public Train getTrainById(@PathVariable("id") String id) {
        return trainRepository.findById(id);
    }

    @PostMapping("/train")
    public Train addTrain(@RequestBody Train train) {
        return trainRepository.save(train);
    }

    @PutMapping("/train/{id}")
    public Train updateTrain(@PathVariable("id")String id, @RequestBody Train train) {
        Train existing = trainRepository.findById(id);

        existing.setDate(train.getDate());
        existing.setId(train.getId());
        existing.setId_route(train.getId_route());
        return trainRepository.save(existing);
    }

    @DeleteMapping("/train/{id}")
    public void deleteTrain(@PathVariable("id") String id) {
        trainRepository.delete(id);
    }
}
