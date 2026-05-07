package ru.vk.recommender.demospringitmo.workshop.controllers.dto;

import java.util.List;

import ru.vk.recommender.demospringitmo.workshop.controllers.dto.notes.NoteDtoLite;

public record TaskDto(
        long id,
        String title,
        boolean done,
        List<NoteDtoLite> notes
) {}