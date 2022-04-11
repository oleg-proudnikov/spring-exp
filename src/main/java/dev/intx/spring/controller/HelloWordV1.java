package dev.intx.spring.controller;

import dev.intx.spring.model.Greeting;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface HelloWordV1 {

    @GetMapping(path = "/hello/{name}", produces = {"application/json"})
    Greeting helloWorld(@PathVariable String name);
}
