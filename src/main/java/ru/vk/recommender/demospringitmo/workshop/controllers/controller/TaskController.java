package ru.vk.recommender.demospringitmo.workshop.controllers.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.vk.recommender.demospringitmo.workshop.controllers.dto.CreateTaskDto;
import ru.vk.recommender.demospringitmo.workshop.controllers.dto.ReplaceTaskDto;
import ru.vk.recommender.demospringitmo.workshop.controllers.dto.TaskStatsDto;
import ru.vk.recommender.demospringitmo.workshop.controllers.dto.UpdateTaskDto;
import ru.vk.recommender.demospringitmo.workshop.controllers.model.Task;
import ru.vk.recommender.demospringitmo.workshop.controllers.service.TaskService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskService taskService;

    //GET /api/tasks?done=true&query=read
    @GetMapping
    public List<Task> getAllTasks(@RequestParam(required = false) Boolean done,
                                  @RequestParam(required = false) String query,
                                  @RequestParam(required = false) String sort)
    {
        return taskService.getTasks(done, query, sort);
    }
    //GET /api/tasks/stats
    @GetMapping("/stats")
    public TaskStatsDto getTaskStats() {
        return taskService.getStats();
    }

    // GET /api/tasks/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable long id) {
        Task task = taskService.getTaskById(id);
        return ResponseEntity.ok(task);
    }

    // POST /api/tasks
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Task createTask(@RequestBody CreateTaskDto request) {
        return taskService.createTask(request);
    }
//
    // PUT /api/tasks/{id} — полная замена
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    public Task replaceTask(@PathVariable long id,
                            @RequestBody ReplaceTaskDto request)
    {
        return taskService.replaceTask(id, request);
    }
//
    // PATCH /api/tasks/{id} — частичное обновление
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{id}")
    public Task patchTask(@PathVariable long id,
                          @RequestBody UpdateTaskDto request)
    {
        return taskService.patchTask(id, request);
    }

    // DELETE /api/tasks/{id}
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable long id) {
        taskService.deleteTask(id);
    }
}
