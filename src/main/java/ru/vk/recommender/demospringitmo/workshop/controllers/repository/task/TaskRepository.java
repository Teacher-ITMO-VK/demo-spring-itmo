package ru.vk.recommender.demospringitmo.workshop.controllers.repository.task;

import ru.vk.recommender.demospringitmo.workshop.controllers.dto.TaskStatsDto;
import ru.vk.recommender.demospringitmo.workshop.controllers.model.Task;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {

    List<Task> findAll(Boolean done, String query, String sort);
    TaskStatsDto getStats();
    Optional<Task> findById(long id);

    Task save(Task task);

    boolean existsById(long id);

    boolean deleteById(long id);
}