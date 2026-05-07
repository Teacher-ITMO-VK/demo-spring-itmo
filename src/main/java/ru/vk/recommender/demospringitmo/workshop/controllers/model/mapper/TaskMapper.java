package ru.vk.recommender.demospringitmo.workshop.controllers.model.mapper;

import ru.vk.recommender.demospringitmo.workshop.controllers.dto.TaskDto;
import ru.vk.recommender.demospringitmo.workshop.controllers.dto.notes.NoteDtoLite;
import ru.vk.recommender.demospringitmo.workshop.controllers.model.jpa.Note;
import ru.vk.recommender.demospringitmo.workshop.controllers.model.jpa.Task;

import java.util.List;

public final class TaskMapper {

    private TaskMapper() {}

    public static TaskDto toDto(Task task) {
        List<NoteDtoLite> notes = task.getNotes().stream()
                .map(TaskMapper::toDto)
                .toList();

        return new TaskDto(
                task.getId(),
                task.getTitle(),
                task.isDone(),
                notes
        );
    }

    public static NoteDtoLite toDto(Note note) {
        return new NoteDtoLite(note.getId(), note.getText());
    }
}