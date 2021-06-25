package com.reactorcourse.demo;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

public class InfiniteFluxTest {
    @Test
    public void itShouldPublishItemsInfinitely() {
        Flux<Integer> testFlux = Flux.interval(Duration.ofNanos(10))
                .take(10)
                .map(Long::intValue);

        StepVerifier.create(testFlux.log())
                .expectSubscription()
                .expectNext(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
                .verifyComplete();
    }
}
