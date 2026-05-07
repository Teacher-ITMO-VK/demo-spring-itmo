package ru.vk.recommender.demospringitmo.workshop.controllers.model.jpa;

import jakarta.persistence.*;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false)
    private String title;

    @Setter
    @Column(nullable = false)
    private boolean done;

    @OneToMany(
            mappedBy = "task", // "task" — владелец связи Note.task (foreign key хранится в notes).
            cascade = CascadeType.ALL, //сохранение task сохранит и notes (когда добавляем note через task).
            orphanRemoval = true, //если note удалили из коллекции task.notes, Hibernate удалит строку из notes.
            fetch = FetchType.LAZY
    )
    private List<Note> notes = new ArrayList<>();

    protected Task() {}

    public Task(String title) {
        this.title = title;
        this.done = false;
    }

    public Task(String title, boolean done) {
        this.title = title;
        this.done = done;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public boolean isDone() { return done; }
    public List<Note> getNotes() { return notes; }

    // удобные методы для поддержания двусторонней связи
    // сохранит в бд по завершении метода, return вернет сущность без ID -> возможны NPE
    public Note addNote(String text) {
        Note note = new Note(this, text);
        notes.add(note);
        return note;
    }

    public boolean removeNoteById(long noteId) {
        return notes.removeIf(n -> n.getId() != null && n.getId() == noteId);
    }
}