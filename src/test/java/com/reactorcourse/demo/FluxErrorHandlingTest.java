package com.reactorcourse.demo;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FluxErrorHandlingTest {
    @Test
    public void itShouldHandleError() {
        Flux<String> testFlux = Flux.just("Some", "text")
                .concatWith(Flux.error(new RuntimeException("Error")))
                .log()
                .onErrorResume(e -> {
                    System.out.println(e);
                    return Flux.just("biba!").log();
                });

        StepVerifier.create(testFlux)
                .expectNext("Some")
                .expectNext("text")
                .expectNext("biba!")
                .verifyComplete();
    }
}
