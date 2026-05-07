package ru.vk.recommender.demospringitmo.workshop.controllers.service.task;

import org.springframework.data.domain.Sort;

public final class TaskSortMapper {

    private TaskSortMapper() {}

    public static Sort toSort(String sort) {
        if (sort == null || sort.isBlank() || sort.equals("id")) {
            return Sort.by(Sort.Direction.ASC, "id");
        }

        return switch (sort) {
            case "title" -> Sort.by(Sort.Direction.ASC, "title");
            case "done" -> Sort.by(Sort.Direction.ASC, "done").and(Sort.by("id"));
            case "id_desc" -> Sort.by(Sort.Direction.DESC, "id");
            case "title_desc" -> Sort.by(Sort.Direction.DESC, "title");
            default -> throw new IllegalArgumentException("Unsupported sort: " + sort);
        };
    }
}