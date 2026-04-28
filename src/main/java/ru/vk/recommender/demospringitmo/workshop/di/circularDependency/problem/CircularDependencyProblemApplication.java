package ru.vk.recommender.demospringitmo.workshop.di.circularDependency.problem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CircularDependencyProblemApplication {

    public static void main(String[] args) {
        SpringApplication.run(CircularDependencyProblemApplication.class, args);
    }
}
