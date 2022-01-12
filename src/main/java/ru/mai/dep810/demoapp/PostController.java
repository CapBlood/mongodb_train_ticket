//package ru.mai.dep810.demoapp;
//
//import java.util.List;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import ru.mai.dep810.demoapp.model.Station;
//import ru.mai.dep810.demoapp.repository.PostElasticRepository;
//
//@RestController
//public class PostController {
//
//    private PostElasticRepository postElasticRepository;
//
//    public PostController(PostElasticRepository postElasticRepository) {
//        this.postElasticRepository = postElasticRepository;
//    }
//
//    @GetMapping("/post")
//    public List<Station> searchPosts(@RequestParam("q") String query) {
//        return postElasticRepository.fullTextSearch(query);
//    }
//
//    @GetMapping("/post/{id}")
//    public Station getPostById(@PathVariable("id") String id) {
//        return postElasticRepository.findById(id);
//    }
//
//    @PostMapping("/post")
//    public Station addPost(@RequestBody Station post) {
//        return postElasticRepository.addToIndex(post);
//    }
//}
