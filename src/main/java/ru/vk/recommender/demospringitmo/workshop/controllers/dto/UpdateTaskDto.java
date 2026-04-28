package ru.vk.recommender.demospringitmo.workshop.controllers.dto;

// for PATCH
public record UpdateTaskDto(
        String title,
        Boolean done
) {}