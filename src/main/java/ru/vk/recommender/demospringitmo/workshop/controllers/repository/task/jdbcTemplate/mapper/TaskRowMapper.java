package ru.vk.recommender.demospringitmo.workshop.controllers.repository.task.jdbcTemplate.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.vk.recommender.demospringitmo.workshop.controllers.model.Task;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TaskRowMapper implements RowMapper<Task> {

    @Override
    public Task mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Task(
                rs.getLong("id"),
                rs.getString("title"),
                rs.getBoolean("done")
        );
    }
}