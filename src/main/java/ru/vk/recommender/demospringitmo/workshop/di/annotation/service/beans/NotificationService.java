package ru.vk.recommender.demospringitmo.workshop.di.annotation.service.beans;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationService {
    public void notifyUser(String message) {
        log.info("NOTIFY: {}", message);
    }
}