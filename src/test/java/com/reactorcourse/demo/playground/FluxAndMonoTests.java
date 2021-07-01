package com.reactorcourse.demo.playground;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static reactor.core.scheduler.Schedulers.parallel;

@SpringBootTest
public class FluxAndMonoTests {
    @Test
    public void itShouldMergeFluxes() {
        Flux<String> firstFlux = Flux.just("Hi", "All");
        Flux<String> secondFlux = Flux.just("How", "is", "it");

        Flux<String> testFlux = Flux.merge(firstFlux, secondFlux)
                .log()
                .flatMap(element -> Flux.just(element, "biba"))
                .subscribeOn(parallel());

        StepVerifier.create(testFlux)
                .expectSubscription()
                .expectNext("Hi", "biba", "All", "biba","How", "biba", "is", "biba", "it", "biba")
                .verifyComplete();
    }
}
