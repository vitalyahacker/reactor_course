package com.reactorcourse.demo;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.util.retry.Retry;

import java.time.Duration;

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
                .expectNext("Some", "text", "biba!")
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
                .expectNext("some", "text", "default")
                .verifyComplete();
    }

    @Test
    public void itShouldDoRetriesOnError() {
        Flux<String> testFlux = Flux.just("some", "text")
                .concatWith(Flux.error(new RuntimeException("error")))
                .onErrorMap(RuntimeException::new)
                .retry(3);

        StepVerifier.create(testFlux.log())
                .expectSubscription()
                .expectNext("some", "text")
                .expectNext("some", "text")
                .expectNext("some", "text")
                .expectNext("some", "text")
                .verifyError(RuntimeException.class);
    }

    @Test
    public void itShouldDoRetriesWithBackoffOnError() {
        Flux<String> testFlux = Flux.just("some", "text")
                .concatWith(Flux.error(new RuntimeException("error")))
                .onErrorMap(RuntimeException::new)
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(2L))); // the backoff is exponential

        StepVerifier.create(testFlux.log())
                .expectSubscription()
                .expectNext("some", "text")
                .expectNext("some", "text")
                .expectNext("some", "text")
                .expectNext("some", "text")
                .verifyError(RuntimeException.class);
    }
}
