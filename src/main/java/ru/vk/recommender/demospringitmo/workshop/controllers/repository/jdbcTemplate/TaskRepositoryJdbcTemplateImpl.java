package ru.vk.recommender.demospringitmo.workshop.controllers.repository.jdbcTemplate;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.vk.recommender.demospringitmo.workshop.controllers.model.Task;
import ru.vk.recommender.demospringitmo.workshop.controllers.repository.TaskRepository;
import ru.vk.recommender.demospringitmo.workshop.controllers.repository.jdbcTemplate.mapper.TaskRowMapper;

import java.util.List;
import java.util.Optional;

@Repository
@Primary
public class TaskRepositoryJdbcTemplateImpl implements TaskRepository {
    private final JdbcTemplate jdbcTemplate;
    private final TaskRowMapper taskRowMapper = new TaskRowMapper();

    public TaskRepositoryJdbcTemplateImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Task> findAll() {
        return jdbcTemplate.query("SELECT id, title, done FROM tasks ORDER BY id", taskRowMapper);
    }

    @Override
    public Optional<Task> findById(long id) {
        return jdbcTemplate.query(
                "SELECT id, title, done FROM tasks WHERE id = ?",
                taskRowMapper,
                id
        ).stream().findFirst();
    }

    /*
    Вариант с queryForObject
    @Override
    public Optional<Task> findById(long id) {
        try {
            Task task = jdbcTemplate.queryForObject(
                    "SELECT id, title, done FROM tasks WHERE id = ?",
                    taskRowMapper,
                    id
            );
            return Optional.ofNullable(task);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
     */


    @Override
    @Transactional
    public Task save(Task task) {
        if (task.id() == 0) {
            Long id = jdbcTemplate.queryForObject(
                    "INSERT INTO tasks(title, done) VALUES (?, ?) RETURNING id",
                    Long.class,
                    task.title(), task.done()
            );
            // id теоретически может быть null, но в норме Postgres вернёт значение всегда
            return new Task(id, task.title(), task.done());
        }

        jdbcTemplate.update(
                "UPDATE tasks SET title = ?, done = ? WHERE id = ?",
                task.title(), task.done(), task.id()
        );
        return task;
    }

    @Override
    public boolean existsById(long id) {
        return Boolean.TRUE.equals(
                jdbcTemplate.queryForObject(
                        "SELECT EXISTS(SELECT 1 FROM tasks WHERE id = ?)",
                        Boolean.class,
                        id
                )
        );
    }

    @Override
    public boolean deleteById(long id) {
        return jdbcTemplate.update("DELETE FROM tasks WHERE id = ?", id) > 0;
    }
}