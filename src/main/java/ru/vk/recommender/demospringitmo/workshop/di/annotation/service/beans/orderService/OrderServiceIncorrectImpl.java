package ru.vk.recommender.demospringitmo.workshop.di.annotation.service.beans.orderService;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.vk.recommender.demospringitmo.workshop.di.annotation.service.beans.NotificationService;
import ru.vk.recommender.demospringitmo.workshop.di.annotation.service.beans.PaymentClient;

/**
 * @author m.lukashev
 */
@Service
@Qualifier("orderServiceIncorrect")
public class OrderServiceIncorrectImpl  implements OrderService {
    private final PaymentClient paymentClient;
    private final NotificationService notificationService;

    public OrderServiceIncorrectImpl(PaymentClient paymentClient, NotificationService notificationService) {
        this.paymentClient = paymentClient;
        this.notificationService = notificationService;
    }

    public void placeOrder(int amount) {
        String result = paymentClient.pay(amount);
        notificationService.notifyUser("Order result " + result);
    }
}
