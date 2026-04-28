package ru.vk.recommender.demospringitmo.workshop.di.configuration.service.beans;


// просто компонент (общий маркер для любого бина)
public class PaymentClient {
    public String pay(int amount) {
        return "PAID " + amount;
    }
}