package dev.intx.spring.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.intx.spring.model.Greeting;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;
import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class HelloWorldControllerTest {

    public static final String HELLO_NAME_ENDPOINT = "/hello/{name}";
    public static final String NAME = "James";
    public static final String APPLICATION_JSON = "application/json";
    public static final String TEXT_PLAIN = "text/plain";

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper mapper;

    @Test
    void shouldReturnJsonGreeting() throws Exception {
        final var response = mvc.perform(get(HELLO_NAME_ENDPOINT, NAME).accept(APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andReturn()
                                .getResponse()
                                .getContentAsString();

        final var greeting = mapper.readValue(response, Greeting.class);

        Assertions.assertThat(greeting).isEqualTo(new Greeting("Hello, " + NAME));
    }

    @Test
    void shouldAllowCrossOriginRequest() throws Exception {
        mvc.perform(get(HELLO_NAME_ENDPOINT, NAME).accept(APPLICATION_JSON)
                                                  .header(HttpHeaders.ORIGIN, "other.com")
                                                  .header(HttpHeaders.REFERER, "http://other.com/"))
           .andExpect(status().isOk());
    }

    @Test
    void shouldSupportOptions() throws Exception {
        mvc.perform(options(HELLO_NAME_ENDPOINT, NAME).accept(TEXT_PLAIN))
           .andExpect(status().isOk())
           .andExpect(header().string("Allow", "GET,HEAD,OPTIONS"));
    }

    @Test
    void shouldNotReturnTextGreeting() throws Exception {
        mvc.perform(get(HELLO_NAME_ENDPOINT, NAME).accept(TEXT_PLAIN))
           .andExpect(status().is(NOT_ACCEPTABLE.value()));
    }

    @Test
    void shouldNotAllowNonGetMethods() throws Exception {
        mvc.perform(post(HELLO_NAME_ENDPOINT, NAME).accept(APPLICATION_JSON))
           .andExpect(status().is(METHOD_NOT_ALLOWED.value()));

        mvc.perform(put(HELLO_NAME_ENDPOINT, NAME).accept(APPLICATION_JSON))
           .andExpect(status().is(METHOD_NOT_ALLOWED.value()));

        mvc.perform(patch(HELLO_NAME_ENDPOINT, NAME).accept(APPLICATION_JSON))
           .andExpect(status().is(METHOD_NOT_ALLOWED.value()));
    }
}
