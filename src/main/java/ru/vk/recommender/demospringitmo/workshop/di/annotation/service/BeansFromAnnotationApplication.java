package ru.vk.recommender.demospringitmo.workshop.di.annotation.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import ru.vk.recommender.demospringitmo.workshop.di.annotation.service.beans.orderService.OrderService;
import ru.vk.recommender.demospringitmo.workshop.di.annotation.service.beans.orderService.OrderServiceImpl;

@SpringBootApplication()
public class BeansFromAnnotationApplication {

    /* Что делает Spring:
    1) создаёт ApplicationContext,
    2) сканирует компоненты (@SpringBootApplication), (package test)
    3) создаёт все бины, настраивает автоконфигурацию,
    4) поднимает встроенный сервер (если это web-приложение),
    5) завершает инициализацию контекста.
     */
    public static void main(String[] args) {
        SpringApplication.run(BeansFromAnnotationApplication.class, args);
    }

    /*
        Во время создания бинов регается этот бин.
        Бинов этого типа может быть несколько, очередь задается через @Order
        Обычно для кейсов: выполнить один раз на старте (как вариант - что-то быстро дернуть, как мы)
        Кейс: заполнить что-то стартовыми данными
     */
    @Bean
    @Order(1)
    CommandLineRunner run(@Qualifier("orderService") OrderService orderService) {
        return args -> orderService.placeOrder(100);
    }

    @Bean
    @Order(2)
    CommandLineRunner runIncorrect(@Qualifier("orderService") OrderService orderService) {
        return args -> orderService.placeOrder(-100);
    }

    @Bean
    @Order(3)
    CommandLineRunner runIncorrectService(@Qualifier("orderServiceIncorrect") OrderService orderService) {
        return args -> orderService.placeOrder(-100);
    }
}
