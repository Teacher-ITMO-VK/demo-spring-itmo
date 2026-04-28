package ru.vk.recommender.demospringitmo.workshop.di.configuration.service.beans;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NotificationService {
    public void notifyUser(String message) {
        log.info("NOTIFY: {}", message);
    }
}