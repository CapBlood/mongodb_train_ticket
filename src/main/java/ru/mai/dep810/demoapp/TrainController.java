package ru.mai.dep810.demoapp;

import java.util.List;

import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
//import ru.mai.dep810.demoapp.repository.PostElasticRepository;
import ru.mai.dep810.demoapp.model.Train;
import ru.mai.dep810.demoapp.repository.TrainElasticRepository;
import springfox.documentation.annotations.ApiIgnore;

@RestController
public class TrainController {

    private TrainElasticRepository trainElasticRepository;

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
