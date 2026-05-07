package ru.vk.recommender.demospringitmo.workshop.controllers.dto;

public record TaskStatsDto(
        long total,
        long done,
        long pending
) {
}