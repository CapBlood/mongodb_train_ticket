package ru.mai.dep810.demoapp;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
//import ru.mai.dep810.demoapp.repository.PostElasticRepository;
import ru.mai.dep810.demoapp.model.Train;
import ru.mai.dep810.demoapp.repository.TrainElasticRepository;

@RestController
public class TrainController {

    private TrainElasticRepository trainElasticRepository;

    public TrainController(TrainElasticRepository trainTicketElasticRepository) {
        this.trainElasticRepository = trainTicketElasticRepository;
    }

    @GetMapping("/train")
    public List<Train> searchTickets(@RequestParam("date") String date,
                                     @RequestParam("from") String from,
                                     @RequestParam("to") String to) {
        return trainElasticRepository.fullTextSearch(date, from, to);
    }

    @GetMapping("/train/{id}")
    public Train getTicketById(@PathVariable("id") String id) {
        return trainElasticRepository.findById(id);
    }

    @PostMapping("/train")
    public Train addTicket(@RequestBody Train train) {
        return trainElasticRepository.addToIndex(train);
    }
}
