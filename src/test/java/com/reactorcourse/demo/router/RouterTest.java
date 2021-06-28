package com.reactorcourse.demo.router;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@SpringBootTest
public class RouterTest {

    @Autowired
    private  RouterConfig routerConfig;

    @Test
    public void itShouldReturnFlux() {
        WebTestClient webTestClient = WebTestClient.bindToRouterFunction(routerConfig.routerFunction()).build();

        Flux<String> responseBody = webTestClient.get().uri("/router/elements")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .exchange()
                .expectStatus().isOk()
                .returnResult(String.class)
                .getResponseBody();

        StepVerifier.create(responseBody)
                .expectSubscription()
                .expectNext("asdf", "kekw", "hi")
                .thenCancel()
                .verify();

    }
}
