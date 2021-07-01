package com.reactorcourse.demo.controller;

import com.reactorcourse.demo.document.Item;
import com.reactorcourse.demo.repo.ItemReactiveRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

@SpringBootTest
@AutoConfigureWebTestClient
public class ItemControllerTest {
    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private ItemReactiveRepository repository;

    @BeforeEach
    public void init() {
        repository.deleteAll()
                .thenMany(
                        repository.saveAll(
                                List.of(
                                        new Item(null, "asd", 52.42),
                                        new Item(null, "Tesla", 66355.65),
                                        new Item("123", "Biba", 65.65)
                                )
                        )
                ).blockLast();
    }

    @Test
    public void itShouldInsertItem() {
        webTestClient.post()
                .uri("/item")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(new Item(null, "Samsung A52", 350.0)), Item.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.description").isEqualTo("Samsung A52")
                .jsonPath("$.price").isEqualTo(350.0);
    }

    @Test
    public void itShouldFindOneItem() {
        webTestClient.get()
                .uri("/item/{id}", "123")
                .exchange()
                .expectBody()
                .jsonPath("$.id").isEqualTo("123")
                .jsonPath("$.description").isEqualTo("Biba")
                .jsonPath("$.price").isEqualTo(65.65);
    }

    @Test
    public void isShouldFindAllItems() {
        webTestClient.get()
                .uri("/item")
                .exchange()
                .expectBodyList(Item.class)
                .hasSize(3);
    }

    @Test
    public void itShouldDeleteItem() {
        webTestClient.delete()
                .uri("/item/{id}", "123")
                .exchange()
                .expectStatus().isNoContent()
                .expectBody(Void.class);
    }
}
