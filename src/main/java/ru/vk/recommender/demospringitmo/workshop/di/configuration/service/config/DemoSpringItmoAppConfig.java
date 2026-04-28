package ru.vk.recommender.demospringitmo.workshop.di.configuration.service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.vk.recommender.demospringitmo.workshop.di.configuration.service.beans.NotificationService;
import ru.vk.recommender.demospringitmo.workshop.di.configuration.service.beans.orderService.OrderService;
import ru.vk.recommender.demospringitmo.workshop.di.configuration.service.beans.orderService.OrderServiceImpl;
import ru.vk.recommender.demospringitmo.workshop.di.configuration.service.beans.PaymentClient;
import ru.vk.recommender.demospringitmo.workshop.di.configuration.service.beans.orderService.OrderServiceIncorrect;

@Configuration
public class DemoSpringItmoAppConfig {

    @Bean
    public PaymentClient paymentClient() {
        return new PaymentClient();
    }

    @Bean
    public NotificationService notificationService() {
        return new NotificationService();
    }

    @Bean
    public OrderService orderService(PaymentClient paymentClient,
                                     NotificationService notificationService) {
        return new OrderServiceImpl(paymentClient, notificationService);
    }

    @Bean
    public OrderService orderServiceIncorrect(PaymentClient paymentClient,
                                     NotificationService notificationService) {
        return new OrderServiceIncorrect(paymentClient, notificationService);
    }
}
