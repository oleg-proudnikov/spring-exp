package dev.intx.spring.service;

import dev.intx.spring.model.Greeting;
import org.springframework.stereotype.Service;

@Service
public class HelloWorldService {

    public Greeting getGreeting(final String name) {
        return new Greeting(String.format("Hello, %s", name));
    }
}
