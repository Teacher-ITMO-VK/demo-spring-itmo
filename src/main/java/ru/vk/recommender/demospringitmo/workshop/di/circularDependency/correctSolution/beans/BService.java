package ru.vk.recommender.demospringitmo.workshop.di.circularDependency.correctSolution.beans;

import org.springframework.stereotype.Service;

@Service
public class BService {
    private final CommonService commonService;

    public BService(CommonService commonService) {
        this.commonService = commonService;
    }
}