package com.reactorcourse.demo.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.test.scheduler.VirtualTimeScheduler;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureWebTestClient
public class ReactiveControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void itShouldReturnFluxResponse() {
        VirtualTimeScheduler.getOrSet();

        Flux<Integer> responseBody = webTestClient.get()
                .uri("/elements/stream")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .exchange()
                .expectStatus().isOk()
                .returnResult(Integer.class)
                .getResponseBody();

        StepVerifier.withVirtualTime(() -> responseBody)
                .expectSubscription()
                .thenAwait(Duration.ofSeconds(4))
                .expectNext(1, 2, 3, 4)
                .verifyComplete();
    }

    @Test
    public void itShouldPublishInfiniteElements() {
        Flux<Long> responseBody = webTestClient.get().uri("/elements/stream/infinite")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .exchange()
                .expectStatus().isOk()
                .returnResult(Long.class)
                .getResponseBody();

        StepVerifier.create(responseBody)
                .expectSubscription()
                .expectNext(0L, 1L, 2L)
                .thenCancel()
                .verify();
    }

    @Test
    public void itShouldPublishOneElement() {
        webTestClient.get().uri("/element")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Integer.class)
                .consumeWith(response -> assertEquals(666, response.getResponseBody()));
    }
}
