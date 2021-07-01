package com.reactorcourse.demo.repo;

import com.reactorcourse.demo.document.Item;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface ItemReactiveRepository extends ReactiveMongoRepository<Item, String> {
    Flux<Item> findItemByPriceBetween(double from, double to);
    Flux<Item> findByDescription(String description);
}
