package ru.mai.dep810.demoapp;

import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.*;
import ru.mai.dep810.demoapp.model.Train;
import ru.mai.dep810.demoapp.repository.TrainElasticRepository;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@RestController
public class TrainController {

    private final TrainElasticRepository trainElasticRepository;

    public TrainController(TrainElasticRepository trainTicketElasticRepository) {
        this.trainElasticRepository = trainTicketElasticRepository;
    }


    @GetMapping("/train")
    @ApiOperation("Получение поезда с заданными параметрами")
    public List<Train> searchTickets(@RequestParam("date") @Parameter(description = "Дата отправления поезда") String date,
                                     @RequestParam("from") @Parameter(description = "Станция отправления поезда") String from,
                                     @RequestParam("to") @Parameter(description = "Станция прибытия поезда") String to) {
        return trainElasticRepository.fullTextSearch(date, from, to);
    }

    @ApiIgnore
    @GetMapping("/train/{id}")
    public Train getTicketById(@PathVariable("id") String id) {
        return trainElasticRepository.findById(id);
    }

    @PostMapping("/train")
    @ApiIgnore
    public Train addTicket(@RequestBody Train train) {
        return trainElasticRepository.addToIndex(train);
    }
}
