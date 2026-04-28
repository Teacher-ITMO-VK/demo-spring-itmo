package ru.vk.recommender.demospringitmo.workshop.di.circularDependency.correctSolution.beans;

import org.springframework.stereotype.Service;

@Service
public class CommonService {

    public String doCommonLogic() {
        return "Doing common job";
    }
}
