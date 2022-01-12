//package ru.mai.dep810.demoapp;
//
//import java.util.ArrayList;
//import java.util.List;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//import ru.mai.dep810.demoapp.model.Route;
//import ru.mai.dep810.demoapp.model.Ticket;
//import ru.mai.dep810.demoapp.repository.StudentRepository;
//
//@RestController
//public class StudentController {
//
//    private StudentRepository studentRepository;
//
//    public StudentController(@Qualifier("hazelcastCachedStudentRepository") StudentRepository studentRepository) {
//        this.studentRepository = studentRepository;
//    }
//
//    private final List<Ticket> students = new ArrayList<>(List.of(
//            new Ticket("1", "John Smith", 21, List.of(new Route("RU", "Moscow", "1st Volokolamkiy pr"))),
//            new Ticket("2", "Steve Howking", 25, List.of(new Route("UK", "London", "Liverpool st")))
//    ));
//
//    @GetMapping("/student")
//    public List<Ticket> getAllStudents() {
//        return studentRepository.findAll();
//    }
//
//    @GetMapping("/student/{id}")
//    public Ticket getStudentById(@PathVariable("id") String id) {
//        return studentRepository.findById(id);
//    }
//
//    @PostMapping("/student")
//    public Ticket addStudent(@RequestBody Ticket student) {
//        return studentRepository.save(student);
//    }
//
//    @PutMapping("/student/{id}")
//    public Ticket updateStudent(@PathVariable("id")String id, @RequestBody Ticket student) {
//        Ticket existing = studentRepository.findById(id);
//        existing.setAddresses(student.getAddresses());
//        existing.setAge(student.getAge());
//        existing.setName(student.getName());
//        return studentRepository.save(existing);
//    }
//
//    @DeleteMapping("/student/{id}")
//    public void deleteStudent(@PathVariable("id") String id) {
//        studentRepository.delete(id);
//    }
//}
