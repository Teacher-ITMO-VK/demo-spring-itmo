package ru.vk.recommender.demospringitmo.workshop.di.circularDependency.correctSolution.beans;

import org.springframework.stereotype.Service;

@Service
public class AService {
    private final CommonService commonService;

    public AService(CommonService commonService) {
        this.commonService = commonService;
    }
}