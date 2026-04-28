package ru.vk.recommender.demospringitmo.workshop.di.circularDependency.problem;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class AService {
    private final BService bService;

    public AService(BService bService) {
        this.bService = bService;
    }
}