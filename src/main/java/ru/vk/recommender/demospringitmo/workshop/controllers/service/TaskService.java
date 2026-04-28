package ru.vk.recommender.demospringitmo.workshop.controllers.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.vk.recommender.demospringitmo.workshop.controllers.dto.CreateTaskDto;
import ru.vk.recommender.demospringitmo.workshop.controllers.dto.ReplaceTaskDto;
import ru.vk.recommender.demospringitmo.workshop.controllers.dto.UpdateTaskDto;
import ru.vk.recommender.demospringitmo.workshop.controllers.model.Task;
import ru.vk.recommender.demospringitmo.workshop.controllers.repository.TaskRepository;

import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;

    public List<Task> getTasks(Boolean done, String query) {
        List<Task> result = taskRepository.findAll().stream()
                .filter(task -> done == null || task.done() == done)
                .filter(task -> query == null || query.isBlank()
                        || task.title().toLowerCase().contains(query.toLowerCase()))
                .toList();

        if (result.isEmpty()) {
            throw new ResponseStatusException(NOT_FOUND, "Tasks not found");
        }

        return result;
    }

    public Task getTaskById(long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Task not found"));
    }

    public Task createTask(CreateTaskDto request) {
        if (request == null || request.title() == null || request.title().isBlank()) {
            throw new ResponseStatusException(BAD_REQUEST, "title must not be blank");
        }

        Task newTask = new Task(0, request.title().trim(), false);
        return taskRepository.save(newTask);
    }

    public Task replaceTask(long id, ReplaceTaskDto request) {
        if (request == null || request.title() == null || request.title().isBlank()) {
            throw new ResponseStatusException(BAD_REQUEST, "title must not be blank");
        }

        if (taskRepository.findById(id).isEmpty()) {
            throw new ResponseStatusException(NOT_FOUND, "Task not found");
        }

        Task replaced = new Task(id, request.title().trim(), request.done());
        return taskRepository.save(replaced);
    }

    public Task patchTask(long id, UpdateTaskDto request) {
        if (request == null) {
            throw new ResponseStatusException(BAD_REQUEST, "request body is required");
        }

        Task existing = taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Task not found"));

        String newTitle = existing.title();
        boolean newDone = existing.done();

        if (request.title() != null) {
            if (request.title().isBlank()) {
                throw new ResponseStatusException(BAD_REQUEST, "title must not be blank");
            }
            newTitle = request.title().trim();
        }

        if (request.done() != null) {
            newDone = request.done();
        }

        Task patched = new Task(existing.id(), newTitle, newDone);
        return taskRepository.save(patched);
    }

    public void deleteTask(long id) {
        boolean deleted = taskRepository.deleteById(id);
        if (!deleted) {
            throw new ResponseStatusException(NOT_FOUND, "Task not found");
        }
    }
}