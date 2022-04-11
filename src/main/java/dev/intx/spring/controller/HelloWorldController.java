package dev.intx.spring.controller;

import dev.intx.spring.model.Greeting;
import dev.intx.spring.service.HelloWorldService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController implements HelloWordV1 {

    private final HelloWorldService service;

    public HelloWorldController(HelloWorldService service) {
        this.service = service;
    }

    @Override
    public Greeting helloWorld(final String name) {
        return service.getGreeting(name);
    }
}
