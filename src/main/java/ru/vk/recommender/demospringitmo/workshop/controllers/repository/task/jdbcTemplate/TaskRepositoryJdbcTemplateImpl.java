package ru.vk.recommender.demospringitmo.workshop.controllers.repository.task.jdbcTemplate;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.vk.recommender.demospringitmo.workshop.controllers.dto.TaskStatsDto;
import ru.vk.recommender.demospringitmo.workshop.controllers.model.Task;
import ru.vk.recommender.demospringitmo.workshop.controllers.repository.task.TaskRepository;
import ru.vk.recommender.demospringitmo.workshop.controllers.repository.task.jdbcTemplate.mapper.TaskRowMapper;

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
    public List<Task> findAll(Boolean done, String query, String sort) {
        String orderBy = resolveOrderBy(sort);

        StringBuilder sql = new StringBuilder("""
        SELECT id, title, done
        FROM tasks
        WHERE 1=1
        """);

        List<Object> params = new java.util.ArrayList<>();

        if (done != null) {
            sql.append(" AND done = ? ");
            params.add(done);
        }

        if (query != null && !query.isBlank()) {
            sql.append(" AND title ILIKE ? ");
            params.add("%" + query + "%");
        }

        sql.append(orderBy);

        return jdbcTemplate.query(sql.toString(), taskRowMapper, params.toArray());
    }

    @Override
    public TaskStatsDto getStats() {
        String sql = """
        SELECT
          COUNT(*) AS total,
          COUNT(*) FILTER (WHERE done = true) AS done,
          COUNT(*) FILTER (WHERE done = false) AS pending
        FROM tasks
        """;

        return jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
                new TaskStatsDto(
                        rs.getInt("total"),
                        rs.getInt("done"),
                        rs.getInt("pending")
                )
        );
    }


    private String resolveOrderBy(String sort) {
        if (sort == null || sort.isBlank()) {
            return " ORDER BY id";
        }

        return switch (sort) {
            case "id" -> " ORDER BY id";
            case "title" -> " ORDER BY title";
            case "done" -> " ORDER BY done, id";
            case "id_desc" -> " ORDER BY id DESC";
            case "title_desc" -> " ORDER BY title DESC";
            default -> throw new IllegalArgumentException("Unsupported sort: " + sort);
        };
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