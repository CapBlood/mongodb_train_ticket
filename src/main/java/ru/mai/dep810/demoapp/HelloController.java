package ru.mai.dep810.demoapp;

import com.hazelcast.core.HazelcastInstance;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;

//@EnableSwagger2
@Api(description = "Чисто для теста")
@RestController
@ApiIgnore
public class HelloController {

    private HazelcastInstance hazelcastInstance;

    public HelloController(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }

    @GetMapping("/hello")
    String hello(@RequestParam(value = "name", required = false, defaultValue = "Anonymous") String name) {
        hazelcastInstance.getTopic("chat").publish("Hello " + name);
        return "Hello " + name;
    }

    @GetMapping("/chat")
    void listMessages(HttpServletResponse response) throws IOException, InterruptedException {
        hazelcastInstance.getTopic("chat").addMessageListener(message -> {
            String msg = message.getPublishingMember().getAddress() + " " + message.getPublishTime() + " " + message.getMessageObject();
            System.out.println(msg);
        });
        Thread.sleep(60000);
    }

}
