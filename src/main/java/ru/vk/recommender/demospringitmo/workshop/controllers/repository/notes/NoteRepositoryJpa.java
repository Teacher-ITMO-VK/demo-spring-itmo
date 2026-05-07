package ru.vk.recommender.demospringitmo.workshop.controllers.repository.notes;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.vk.recommender.demospringitmo.workshop.controllers.model.jpa.Note;

public interface NoteRepositoryJpa extends JpaRepository<@NonNull Note, @NonNull Long> {
    boolean existsByIdAndTask_Id(Long id, Long taskId);
    void deleteByIdAndTask_Id(Long id, Long taskId);
}