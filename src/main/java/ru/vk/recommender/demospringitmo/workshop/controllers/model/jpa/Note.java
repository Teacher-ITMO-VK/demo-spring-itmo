package ru.vk.recommender.demospringitmo.workshop.controllers.model.jpa;

import jakarta.persistence.*;
import lombok.Setter;

@Entity
@Table(name = "notes")
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Задача находится, но не сериализуется в JSON из‑за бесконечной рекурсии по связи Task
    // -> notes -> task -> notes ->
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @Setter
    @Column(nullable = false)
    private String text;

    protected Note() {}

    public Note(Task task, String text) {
        this.task = task;
        this.text = text;
    }

    public Long getId() { return id; }
    public Task getTask() { return task; }
    public String getText() { return text; }
}