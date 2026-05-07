package ru.vk.recommender.demospringitmo.workshop.controllers.service.note;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import ru.vk.recommender.demospringitmo.workshop.controllers.dto.notes.NoteCreateRequest;
import ru.vk.recommender.demospringitmo.workshop.controllers.dto.notes.NoteDto;
import ru.vk.recommender.demospringitmo.workshop.controllers.model.jpa.Note;
import ru.vk.recommender.demospringitmo.workshop.controllers.model.jpa.Task;
import ru.vk.recommender.demospringitmo.workshop.controllers.repository.notes.NoteRepositoryJpa;
import ru.vk.recommender.demospringitmo.workshop.controllers.repository.task.orm.TaskRepositoryJpa;

import static org.springframework.http.HttpStatus.NOT_FOUND;


@Service
public class NoteService {
    private final TaskRepositoryJpa taskRepository;
    private final NoteRepositoryJpa noteRepository;

    public NoteService(TaskRepositoryJpa taskRepository, NoteRepositoryJpa noteRepository) {
        this.taskRepository = taskRepository;
        this.noteRepository = noteRepository;
    }

    @Transactional
    public NoteDto addNote(long taskId, NoteCreateRequest req) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Task not found"));

        Note saved = new Note(task, req.text());
      //  Note saved = noteRepository.save(note); // INSERT происходит сразу (в рамках транзакции)

        task.addNote(saved.getText());
        return new NoteDto(-1, task.getId(), saved.getText());
    }

    @Transactional
    public void deleteNote(long taskId, long noteId) {
        // Вариант 1: удалить через NoteRepository (просто и эффективно)
        if (!noteRepository.existsByIdAndTask_Id(noteId, taskId)) {
            throw new ResponseStatusException(NOT_FOUND, "Note not found");
        }
        noteRepository.deleteByIdAndTask_Id(noteId, taskId);

        // Вариант 2 (альтернатива): через task.notes + orphanRemoval
        // Task task = taskRepository.findById(taskId)...;
        // boolean removed = task.removeNoteById(noteId);
        // if (!removed) throw 404;
    }
}