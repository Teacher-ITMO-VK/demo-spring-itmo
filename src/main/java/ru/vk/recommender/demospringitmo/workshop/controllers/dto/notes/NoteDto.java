package ru.vk.recommender.demospringitmo.workshop.controllers.dto.notes;

public record NoteDto(
        long id,
        long taskId,
        String text
)
{
}