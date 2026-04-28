package ru.vk.recommender.demospringitmo.workshop.di.configuration.service;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import ru.vk.recommender.demospringitmo.workshop.di.configuration.service.beans.orderService.OrderService;

@SpringBootApplication
public class BeansFromConfigApplication {

    public static void main(String[] args) {
        SpringApplication.run(BeansFromConfigApplication.class, args);
    }

    @Bean
    @Order(1)
    CommandLineRunner run(OrderService orderService) {
        return args -> orderService.placeOrder(100);
    }

    @Bean
    @Order(2)
    CommandLineRunner runCorrect(OrderService orderService) {
        return args -> orderService.placeOrder(-100);
    }

    @Bean
    @Order(3)
    CommandLineRunner runIncorrect(OrderService orderServiceIncorrect) {
        return args -> orderServiceIncorrect.placeOrder(-100);
    }
}
