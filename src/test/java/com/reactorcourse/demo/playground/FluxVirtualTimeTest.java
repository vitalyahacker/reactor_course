package com.reactorcourse.demo.playground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.test.scheduler.VirtualTimeScheduler;

import java.time.Duration;

public class FluxVirtualTimeTest {

    @Test
    public void itShouldUseVirtualTime() {

        VirtualTimeScheduler.getOrSet();

        Flux<Integer> testFlux = Flux.interval(Duration.ofSeconds(1))
                .take(3)
                .map(Long::intValue);

        StepVerifier.withVirtualTime(testFlux::log)
                .expectSubscription()
                .thenAwait(Duration.ofSeconds(3))
                .expectNext(0, 1, 2)
                .verifyComplete();
    }
}
