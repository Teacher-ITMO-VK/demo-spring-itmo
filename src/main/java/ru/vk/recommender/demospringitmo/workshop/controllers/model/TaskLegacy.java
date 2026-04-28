package ru.vk.recommender.demospringitmo.workshop.controllers.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Deprecated
@Getter
@EqualsAndHashCode
@ToString
@RequiredArgsConstructor
public class TaskLegacy {
    private final long id;
    private final String title;
    private final boolean done;
}
