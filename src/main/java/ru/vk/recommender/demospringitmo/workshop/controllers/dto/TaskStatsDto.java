package ru.vk.recommender.demospringitmo.workshop.controllers.dto;

public record TaskStatsDto(
        int total,
        int done,
        int pending
) {
}