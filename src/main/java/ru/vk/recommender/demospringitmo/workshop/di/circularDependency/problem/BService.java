package ru.vk.recommender.demospringitmo.workshop.di.circularDependency.problem;

import org.springframework.stereotype.Service;

@Service
public class BService {
    private final AService aService;

    public BService(AService aService) {
        this.aService = aService;
    }
}