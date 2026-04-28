package ru.vk.recommender.demospringitmo.workshop.di.annotation.service.beans;

import org.springframework.stereotype.Component;

@Component
// просто компонент (общий маркер для любого бина)
public class PaymentClient {
    public String pay(int amount) {
        return "PAID " + amount;
    }
}