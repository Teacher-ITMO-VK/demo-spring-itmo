package ru.vk.recommender.demospringitmo.workshop.controllers.repository.task.orm;

import java.util.List;

import lombok.NonNull;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.vk.recommender.demospringitmo.workshop.controllers.dto.TaskStatsDto;
import ru.vk.recommender.demospringitmo.workshop.controllers.model.jpa.Task;

public interface TaskRepositoryJpa extends JpaRepository<@NonNull Task, @NonNull Long> {

    /* SQL Аналог

        SELECT t.id, t.title, t.done
    FROM tasks t
    WHERE ($1 IS NULL OR t.done = $1)
      AND ($2 IS NULL OR $2 = '' OR LOWER(t.title) LIKE LOWER('%' || $2 || '%'))
    -- + ORDER BY из Sort:
    -- ORDER BY t.id ASC|DESC, t.title ASC|DESC, t.done ASC|DESC ...
     */
    @Query("""
            SELECT t
            FROM Task t
            WHERE (:done IS NULL OR t.done = :done)
              AND (:query IS NULL OR :query = '' OR LOWER(t.title) LIKE LOWER(CONCAT('%', :query, '%')))
            """)
    List<Task> findAllFiltered(Boolean done, String query, Sort sort);

    /*
    SQL Аналог:

    SELECT
      COUNT(*) AS total,
      COUNT(*) FILTER (WHERE t.done = true)  AS done,
      COUNT(*) FILTER (WHERE t.done = false) AS pending
    FROM tasks t;
     */
    @Query("""
    SELECT new ru.vk.recommender.demospringitmo.workshop.controllers.dto.TaskStatsDto(
        COUNT(t),
        SUM(CASE WHEN t.done = true THEN 1 ELSE 0 END),
        SUM(CASE WHEN t.done = false THEN 1 ELSE 0 END)
    )
    FROM Task t
    """)
    TaskStatsDto getStats();

    @Query("""
    SELECT DISTINCT t
    FROM Task t
    LEFT JOIN FETCH t.notes n
    WHERE (:done IS NULL OR t.done = :done)
      AND (:query IS NULL OR :query = '' OR LOWER(t.title) LIKE LOWER(CONCAT('%', :query, '%')))
    """)
    List<Task> findAllFilteredWithNotes(Boolean done, String query, Sort sort);
}