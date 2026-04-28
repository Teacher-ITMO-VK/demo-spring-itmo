package ru.vk.recommender.demospringitmo.workshop.controllers.repository;

import ru.vk.recommender.demospringitmo.workshop.controllers.model.Task;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {

    List<Task> findAll();

    Optional<Task> findById(long id);

    Task save(Task task);

    boolean existsById(long id);

    boolean deleteById(long id);
}