package ru.vk.recommender.demospringitmo.workshop.di.configuration.service.beans.orderService;

import org.springframework.stereotype.Service;
import ru.vk.recommender.demospringitmo.workshop.di.configuration.service.beans.NotificationService;
import ru.vk.recommender.demospringitmo.workshop.di.configuration.service.beans.PaymentClient;

/**
 * @author m.lukashev
 */
public class OrderServiceIncorrect implements OrderService {
    private final PaymentClient paymentClient;
    private final NotificationService notificationService;

    public OrderServiceIncorrect(PaymentClient paymentClient, NotificationService notificationService) {
        this.paymentClient = paymentClient;
        this.notificationService = notificationService;
    }

    public void placeOrder(int amount) {
        String result = paymentClient.pay(amount);
        notificationService.notifyUser("Order result " + result);
    }
}
