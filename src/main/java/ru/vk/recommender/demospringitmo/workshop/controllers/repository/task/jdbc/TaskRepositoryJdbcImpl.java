package ru.vk.recommender.demospringitmo.workshop.controllers.repository.task.jdbc;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.vk.recommender.demospringitmo.workshop.controllers.dto.TaskStatsDto;
import ru.vk.recommender.demospringitmo.workshop.controllers.model.Task;
import ru.vk.recommender.demospringitmo.workshop.controllers.repository.task.TaskRepository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
//@Primary
public class TaskRepositoryJdbcImpl implements TaskRepository {

    private final DataSource dataSource;

    public TaskRepositoryJdbcImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<Task> findAll(Boolean done, String query, String sort) {
        String sql = "SELECT id, title, done FROM tasks ORDER BY id";

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            List<Task> result = new ArrayList<>();
            while (rs.next()) {
                result.add(mapTask(rs));
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to findAll tasks", e);
        }
    }

    @Override
    public TaskStatsDto getStats() {
        return null;
    }

    @Override
    public Optional<Task> findById(long id) {
        String sql = "SELECT id, title, done FROM tasks WHERE id = ?";

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapTask(rs));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find task by id=" + id, e);
        }
    }

    @Override
    @Transactional
    public Task save(Task task) {
        if (task.id() == 0) {
            return insert(task);
        } else {
            update(task);
            return task;
        }
    }

    private Task insert(Task task) {
        // Postgres-специфично, но очень удобно: RETURNING id
        String sql = "INSERT INTO tasks(title, done) VALUES (?, ?) RETURNING id";

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, task.title());
            ps.setBoolean(2, task.done());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    long id = rs.getLong(1);
                    return new Task(id, task.title(), task.done());
                }
                throw new SQLException("INSERT did not return id");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert task", e);
        }
    }

    private void update(Task task) {
        String sql = "UPDATE tasks SET title = ?, done = ? WHERE id = ?";
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, task.title());
            ps.setBoolean(2, task.done());
            ps.setLong(3, task.id());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update task id=" + task.id(), e);
        }
    }

    @Override
    public boolean existsById(long id) {
        String sql = "SELECT EXISTS(SELECT 1 FROM tasks WHERE id = ?)";

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getBoolean(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to existsById id=" + id, e);
        }
    }

    @Override
    public boolean deleteById(long id) {
        String sql = "DELETE FROM tasks WHERE id = ?";

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, id);

            int updated = ps.executeUpdate();
            return updated > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to deleteById id=" + id, e);
        }
    }

    private Task mapTask(ResultSet rs) throws SQLException {
        return new Task(
                rs.getLong("id"),
                rs.getString("title"),
                rs.getBoolean("done")
        );
    }
}