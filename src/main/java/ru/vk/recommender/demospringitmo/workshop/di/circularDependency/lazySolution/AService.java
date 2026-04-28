package ru.vk.recommender.demospringitmo.workshop.di.circularDependency.lazySolution;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class AService {
    private final BService bService;

    /*
        Spring внедрит прокси и создаст реальный бин позже.
        Важно: это не лечит дизайн, а обходит проблему.
    */
    public AService(@Lazy BService bService) {
        this.bService = bService;
    }
}