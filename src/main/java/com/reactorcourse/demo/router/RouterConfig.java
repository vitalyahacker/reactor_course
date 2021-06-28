package com.reactorcourse.demo.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;

import java.time.Duration;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterConfig {

    @Bean
    public RouterFunction<ServerResponse> routerFunction() {
        return route(
                GET("/router/elements"),
                serverRequest ->
                        ServerResponse.ok()
                                .contentType(MediaType.TEXT_EVENT_STREAM)
                                .body(
                                        Flux.just("asdf", "kekw", "hi").delayElements(Duration.ofSeconds(1)).log(),
                                        String.class
                                )
        ).and(
                route(
                        GET("/router/elements/{id}"),
                        serverRequest -> ServerResponse.ok()
                                .body(
                                        Flux.just(serverRequest.pathVariable("id")),
                                        String.class
                                )
                )
        );
    }
}
