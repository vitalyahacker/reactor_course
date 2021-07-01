package com.reactorcourse.demo.repo;

import com.reactorcourse.demo.document.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

@DataMongoTest
public class ItemReactiveRepositoryTest {

    private static final double EXPECTED_PRICE = 99.99;

    @Autowired
    private ItemReactiveRepository repository;

    private static final List<Item> items = List.of(
            new Item("1", "bib", 5.665),
            new Item("2", "hey", 6.665),
            new Item("3", "bob", 7.3)
    );

    @BeforeEach
    public void init() {
        repository.deleteAll()
                .then(Mono.just(items))
                .flatMapMany(itemsList -> repository.saveAll(itemsList))
                .blockLast();
    }

    @Test
    public void itShouldGetAllItems() {
        StepVerifier.create(repository.findAll())
                .expectSubscription()
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    public void itShouldGetItemById() {
        StepVerifier.create(repository.findById("2"))
                .expectSubscription()
                .expectNextMatches(item -> item.getDescription().equals("hey"))
                .verifyComplete();
    }

    @Test
    public void itShouldFindItemByPriceRange() {
        StepVerifier.create(repository.findItemByPriceBetween(5.0, 7.0))
                .expectSubscription()
                .expectNextCount(2)
                .verifyComplete();

        StepVerifier.create(repository.findItemByPriceBetween(5.0, 6.0))
                .expectSubscription()
                .expectNextCount(1)
                .verifyComplete();

        StepVerifier.create(repository.findItemByPriceBetween(5.0, 8.0))
                .expectSubscription()
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    public void itShouldSaveItemWithNewId() {
        Item item = new Item(null, "asdfe", 66.5);
        Mono<Item> savedItem = repository.save(item);

        StepVerifier.create(savedItem.log("Check: "))
                .expectSubscription()
                .expectNextMatches(saved -> saved.getId() != null && saved.getDescription().equals("asdfe"))
                .verifyComplete();
    }

    @Test
    public void itShouldUpdateExistingItem() {
        Flux<Item> updateItemFlux = repository.findByDescription("bob")
                .map(item -> {
                    item.setPrice(EXPECTED_PRICE);
                    return item;
                })
                .flatMap(repository::save);

        StepVerifier.create(updateItemFlux.log())
                .expectSubscription()
                .expectNextMatches(item -> item.getPrice() == EXPECTED_PRICE)
                .verifyComplete();
    }

    @Test
    public void itShouldDeleteItemById() {
        String id = "3";
        Flux<Item> itemFlux = repository.deleteById(id).thenMany(repository.findAll());

        StepVerifier.create(itemFlux.log())
                .expectSubscription()
                .expectNextCount(2)
                .verifyComplete();
    }
}
