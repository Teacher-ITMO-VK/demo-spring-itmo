package ru.vk.recommender.demospringitmo.workshop.di.annotation.service.beans.orderService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.vk.recommender.demospringitmo.workshop.di.annotation.service.beans.NotificationService;
import ru.vk.recommender.demospringitmo.workshop.di.annotation.service.beans.PaymentClient;

@Service
@Slf4j
@Qualifier("orderService")
// сервис - слой бизнес логики
public class OrderServiceImpl implements OrderService{
    private final PaymentClient paymentClient;
    private final NotificationService notificationService;

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