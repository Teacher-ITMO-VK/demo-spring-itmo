package ru.vk.recommender.demospringitmo.workshop.controllers.model;

// все поля final, нет setter, нельзя изменить после создания
// автоматическая реализация equals, hashCode, toString
// entity - сущность в БД
public record Task(
        long id,
        String title,
        boolean done
) {}