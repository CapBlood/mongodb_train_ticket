//package ru.mai.dep810.demoapp.repository;
//
//import com.hazelcast.core.HazelcastInstance;
//import com.hazelcast.map.IMap;
//import java.util.List;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.stereotype.Repository;
//import ru.mai.dep810.demoapp.model.Ticket;
//
//@Repository
//public class HazelcastCachedStudentRepository implements StudentRepository {
//    private final StudentRepository delegate;
//    private final IMap<String, Ticket> cache;
//
//    public HazelcastCachedStudentRepository(@Qualifier("mongoStudentRepository") StudentRepository delegate,
//                                            @Autowired HazelcastInstance hazelcastInstance) {
//        this.cache = hazelcastInstance.getMap("student");
//        this.delegate = delegate;
//    }
//
//    @Override
//    public List<Ticket> findAll() {
//        return delegate.findAll();
//    }
//
//    @Override
//    public Ticket findById(String id) {
//        return cache.computeIfAbsent(id, delegate::findById);
//    }
//
//    @Override
//    public Ticket save(Ticket student) {
//        cache.lock(student.getId());
//        try {
//            Ticket s = delegate.save(student);
//            // cache.put(s.getId(), s);
//            cache.remove(student.getId());
//            return s;
//        } finally {
//            cache.unlock(student.getId());
//        }
//    }
//
//    @Override
//    public void delete(String id) {
//        delegate.delete(id);
//        cache.remove(id);
//    }
//}
