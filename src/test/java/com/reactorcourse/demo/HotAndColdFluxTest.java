package com.reactorcourse.demo;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class HotAndColdFluxTest {
    @Test
    public void itShouldCreateColdFlux() throws InterruptedException {
        Flux<String> testFlux = Flux.just("Some", "text", "here")
                .delayElements(Duration.ofSeconds(1))
                .log();

        testFlux.subscribe(System.out::println);
        TimeUnit.SECONDS.sleep(3);
        testFlux.subscribe(System.out::println);
        TimeUnit.SECONDS.sleep(3);
    }

    @Test
    public void itShouldCreateHotFlux() throws InterruptedException {
        Flux<String> testFlux = Flux.just("Some", "text", "here", "hello", "world")
                .delayElements(Duration.ofSeconds(1))
                .log();

        ConnectableFlux<String> connectableFlux = testFlux.publish();
        connectableFlux.connect();

        connectableFlux.subscribe(item -> System.out.println("First sub: " + item));
        TimeUnit.SECONDS.sleep(4);
        connectableFlux.subscribe(item -> System.out.println("Second sub: " + item));
        TimeUnit.SECONDS.sleep(3);

    }
}
