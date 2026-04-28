package ru.vk.recommender.demospringitmo.workshop.di.configuration.service.beans.orderService;

import lombok.extern.slf4j.Slf4j;
import ru.vk.recommender.demospringitmo.workshop.di.configuration.service.beans.NotificationService;
import ru.vk.recommender.demospringitmo.workshop.di.configuration.service.beans.PaymentClient;

@Slf4j
public class OrderServiceImpl implements OrderService {
    private final PaymentClient paymentClient;
    private final NotificationService
            notificationService;

    public OrderServiceImpl(PaymentClient paymentClient, NotificationService notificationService) {
        this.paymentClient = paymentClient;
        this.notificationService = notificationService;
    }

    public void placeOrder(int amount) {
        if (amount <= 0) {
            log.error("Incorrect amount: {}", amount);
            return;
        }
        String result = paymentClient.pay(amount);
        notificationService.notifyUser("Order result " + result);
    }
}