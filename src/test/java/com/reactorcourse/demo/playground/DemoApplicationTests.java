package com.reactorcourse.demo.playground;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static reactor.core.scheduler.Schedulers.parallel;

@SpringBootTest
class DemoApplicationTests {


    @Test
    void verifyThatFluxWorks() {

        Flux<String> stringFlux = Flux.just("Hi", "Biba", "Boba")
                .log();

        StepVerifier.create(stringFlux)
                .expectNext("Hi")
                .expectNext("Biba")
                .expectNext("Boba")
                .verifyComplete();
    }

    @Test
    void verifyThatFluxThrowsError() {

        Flux<String> stringFlux = Flux.just("Hi", "Biba")
                .log()
                .concatWith(Flux.error(new RuntimeException("Whoops!")))
                .concatWith(Flux.just("Boba"));

        StepVerifier.create(stringFlux)
                .expectNext("Hi")
                .expectNext("Biba")
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    public void biba() {
        Flux.just("Hi")
                .log()
                .map(item -> {
                    System.out.printf("Current thread id: %d%n", Thread.currentThread().getId());
                    return item;
                })
                .subscribeOn(parallel())
                .map(item -> {
                    System.out.printf("Current thread id: %d%n", Thread.currentThread().getId());
                    System.out.printf("Current thread group: %s%n", Thread.currentThread().getThreadGroup().getName());
                    return item;
                })
                .subscribe(System.out::println, System.err::println);

    }
}
