package ru.vk.recommender.demospringitmo.workshop.controllers.repository.task.inMemory;

import org.springframework.stereotype.Repository;
import ru.vk.recommender.demospringitmo.workshop.controllers.dto.TaskStatsDto;
import ru.vk.recommender.demospringitmo.workshop.controllers.model.Task;
import ru.vk.recommender.demospringitmo.workshop.controllers.repository.task.TaskRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class TaskRepositoryInMemoryImpl implements TaskRepository {
    private final Map<Long, Task> storage = new ConcurrentHashMap<>();
    private final AtomicLong seq = new AtomicLong(0);

    public TaskRepositoryInMemoryImpl() {
        save(new Task(0, "Read Spring docs", false));
        save(new Task(0, "Write controller", true));
    }
// STAGE 1: GET REQUESTS
    public List<Task> findAll(Boolean done, String query, String sort) {
        return storage.values().stream()
                .sorted(Comparator.comparingLong(Task::id))
                .toList();
    }

    @Override
    public TaskStatsDto getStats() {
        return null;
    }

    public Optional<Task> findById(long id) {
        return Optional.ofNullable(storage.get(id));
    }
//
// STAGE 2 : POST REQUESTS
    public Task save(Task task) {
        long id = task.id();
        if (id == 0) {
            id = seq.incrementAndGet();
        }
        Task saved = new Task(id, task.title(), task.done());
        storage.put(id, saved);
        return saved;
    }

    public boolean existsById(long id) {
        return storage.containsKey(id);
    }
//
// STAGE 3: DELETE REQUESTS
    public boolean deleteById(long id) {
        return storage.remove(id) != null;
    }
}