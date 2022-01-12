//package ru.mai.dep810.demoapp.repository;
//
//import com.github.benmanes.caffeine.cache.Cache;
//import com.github.benmanes.caffeine.cache.Caffeine;
//import java.util.List;
//import java.util.concurrent.TimeUnit;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.stereotype.Repository;
//import ru.mai.dep810.demoapp.model.Ticket;
//
//@Repository
//public class CachedStudentRepository implements StudentRepository {
//    private final StudentRepository delegate;
////    private final ConcurrentHashMap<String, Student> cache = new ConcurrentHashMap<>();
//    private final Cache<String, Ticket> cache = Caffeine.newBuilder()
//        .maximumSize(100)
//        .expireAfterAccess(1, TimeUnit.HOURS)
//        .expireAfterWrite(3, TimeUnit.DAYS)
//        .recordStats()
//        .build(this::findById);
//
//    public CachedStudentRepository(@Qualifier("mongoStudentRepository") StudentRepository delegate) {
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
//        // return cache.computeIfAbsent(id, delegate::findById);
//        return cache.get(id, this::findById);
//    }
//
//    @Override
//    public Ticket save(Ticket student) {
//        Ticket s = delegate.save(student);
//        // cache.put(s.getId(), s);
//        cache.invalidate(student.getId());
//        return s;
//    }
//
//    @Override
//    public void delete(String id) {
//        delegate.delete(id);
//        cache.invalidate(id);
//    }
//}
