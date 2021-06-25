package com.reactorcourse.demo;

import reactor.core.publisher.Mono;

public class Something {
    public Mono<String> something() {
        return Mono.empty();
    }

}
