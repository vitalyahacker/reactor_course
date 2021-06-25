package com.reactorcourse.demo;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FluxErrorHandlingTest {
    @Test
    public void itShouldHandleError() {
        Flux<String> testFlux = Flux.just("Some", "text")
                .concatWith(Flux.error(new RuntimeException("Error")))
                .onErrorResume(e -> {
                    System.out.println(e);
                    return Flux.just("biba!");
                });

        StepVerifier.create(testFlux.log())
                .expectNext("Some")
                .expectNext("text")
                .expectNext("biba!")
                .verifyComplete();
    }

    @Test
    public void itShouldResumeWithDefaultOnError() {
        Flux<String> testFlux = Flux.just("some", "text")
                .concatWith(Flux.error(new RuntimeException("error")))
                .concatWith(Flux.just("never accessed"))
                .onErrorReturn("default");

        StepVerifier.create(testFlux.log())
                .expectSubscription()
                .expectNext("some")
                .expectNext("text")
                .expectNext("default")
                .verifyComplete();
    }
}
