package com.reactorcourse.demo.repo;

import com.reactorcourse.demo.document.Item;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import reactor.test.StepVerifier;

@DataMongoTest
public class ItemReactiveRepositoryTest {

    @Autowired
    private ItemReactiveRepository repository;

    @Test
    public void itShouldGetAllItems() {
        repository.save(new Item("asd", "biba",6.665)).block();
        StepVerifier.create(repository.findAll().log())
                .expectSubscription()
                .expectNextCount(1)
                .expectNextMatches(item -> item.getDescription().equals("biba"))
                .expectComplete();
    }
}
