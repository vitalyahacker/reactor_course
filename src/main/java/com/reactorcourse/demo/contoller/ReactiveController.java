package com.reactorcourse.demo.contoller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
public class ReactiveController {

    @GetMapping("elements")
    public Flux<Integer> getElements() {
        return Flux.just(1, 2, 3, 4)
                .delayElements(Duration.ofSeconds(1))
                .log();
    }

    @GetMapping("element")
    public Mono<Integer> getElement() {
        return Mono.just(666).log();
    }

    @GetMapping(value = "elements/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Integer> getElementsStream() {
        return Flux.just(1, 2, 3, 4)
                .delayElements(Duration.ofSeconds(1))
                .log();
    }

    @GetMapping(value = "elements/stream/infinite", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Long> getElementsStreamInfinite() {
        return Flux.interval(Duration.ofSeconds(1))
                .log();
    }
}
