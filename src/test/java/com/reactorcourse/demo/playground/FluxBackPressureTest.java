package com.reactorcourse.demo.playground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FluxBackPressureTest {

    @Test
    public void itShouldDoBackpressureForTesting() {
        Flux<Integer> testFlux = Flux.range(0, 10);

        StepVerifier.create(testFlux.log())
                .expectSubscription()
                .thenRequest(1)
                .expectNext(0)
                .thenRequest(1)
                .expectNext(1)
                .thenRequest(1)
                .expectNext(2)
                .thenCancel()
                .verify();
    }

    @Test
    public void itShouldDoBackpressureForReal() {
        Flux.range(0, 10)
                .log()
                .subscribe(
                        System.out::println,
                        System.err::println,
                        () -> System.out.println("Complete"),
                        subscription -> subscription.request(3)
                );
    }
}
