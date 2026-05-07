package ru.vk.recommender.demospringitmo.workshop.controllers.service.task;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.vk.recommender.demospringitmo.workshop.controllers.dto.CreateTaskDto;
import ru.vk.recommender.demospringitmo.workshop.controllers.dto.ReplaceTaskDto;
import ru.vk.recommender.demospringitmo.workshop.controllers.dto.TaskDto;
import ru.vk.recommender.demospringitmo.workshop.controllers.dto.TaskStatsDto;
import ru.vk.recommender.demospringitmo.workshop.controllers.dto.UpdateTaskDto;
import ru.vk.recommender.demospringitmo.workshop.controllers.model.jpa.Task;
import ru.vk.recommender.demospringitmo.workshop.controllers.model.mapper.TaskMapper;
import ru.vk.recommender.demospringitmo.workshop.controllers.repository.task.orm.TaskRepositoryJpa;

import java.util.Comparator;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepositoryJpa taskRepository;

    @Transactional(readOnly = true)
    public List<TaskDto> getTasks(Boolean done, String query, String sort) {
        Sort s = TaskSortMapper.toSort(sort);
        // FIX N+1 ERROR
         List<Task> result = taskRepository.findAllFilteredWithNotes(done, query, s);
        // N+1 ERROR DETECTED

        // 1й запрос - 3 таски  (1)
        // 2 3 4 (3 запроса) -> N (==3 ) + 1
      //  List<Task> result = taskRepository.findAllFiltered(done, query, s);
        if (result.isEmpty()) {
            throw new ResponseStatusException(NOT_FOUND, "Tasks not found");
        }

        return result.stream().map(TaskMapper::toDto).toList(); // toDto читает notes
    }

    public TaskStatsDto getStats() {
        return taskRepository.getStats();
    }

    //ВОЗВРАЩАЕМ DTO -> Чтобы избежать циклической зависимость ентрей
    @Transactional(readOnly = true)
    public TaskDto getTaskById(long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Task not found"));

        return TaskMapper.toDto(task);
    }

    @Transactional
    public Task createTask(CreateTaskDto request) {
        if (request == null || request.title() == null || request.title().isBlank()) {
            throw new ResponseStatusException(BAD_REQUEST, "title must not be blank");
        }

        Task newTask = new Task(request.title().trim(), false);
        return taskRepository.save(newTask);
    }

    @Transactional
    public Task replaceTask(long id, ReplaceTaskDto request) {
        if (request == null || request.title() == null || request.title().isBlank()) {
            throw new ResponseStatusException(BAD_REQUEST, "title must not be blank");
        }

        Task existing = taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Task not found"));

        existing.setTitle(request.title().trim());
        existing.setDone(request.done());

        return existing; // UPDATE на commit
    }

    @Transactional
    public Task patchTask(long id, UpdateTaskDto request) {
        if (request == null) {
            throw new ResponseStatusException(BAD_REQUEST, "request body is required");
        }

        Task existing = taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Task not found"));

        if (request.title() != null) {
            if (request.title().isBlank()) {
                throw new ResponseStatusException(BAD_REQUEST, "title must not be blank");
            }
            existing.setTitle(request.title().trim());
        }

        if (request.done() != null) {
            existing.setDone(request.done());
        }

        return existing; // UPDATE на commit
    }

    public void deleteTask(long id) {
        taskRepository.deleteById(id);
    }

    @Deprecated
    //сортировку перенесли в SQL
    private Comparator<Task> resolveSortComparator(String sort) {
        if (sort == null || sort.isBlank() || sort.equals("id")) {
            return Comparator.comparingLong(Task::getId);
        }
        if (sort.equals("title")) {
            return Comparator.comparing((Task task) -> task.getTitle().toLowerCase())
                    .thenComparingLong(Task::getId);
        }
        throw new ResponseStatusException(BAD_REQUEST, "Unsupported sort. Use: id or title");
    }
}