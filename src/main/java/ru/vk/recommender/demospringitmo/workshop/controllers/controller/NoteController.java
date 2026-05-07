package ru.vk.recommender.demospringitmo.workshop.controllers.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.vk.recommender.demospringitmo.workshop.controllers.dto.notes.NoteCreateRequest;
import ru.vk.recommender.demospringitmo.workshop.controllers.dto.notes.NoteDto;
import ru.vk.recommender.demospringitmo.workshop.controllers.service.note.NoteService;

@RestController
@RequestMapping("/api/tasks/{taskId}/notes")
public class NoteController {
    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NoteDto add(@PathVariable long taskId, @RequestBody NoteCreateRequest req) {
        return noteService.addNote(taskId, req);
    }

    @DeleteMapping("/{noteId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long taskId, @PathVariable long noteId) {
        noteService.deleteNote(taskId, noteId);
    }
}