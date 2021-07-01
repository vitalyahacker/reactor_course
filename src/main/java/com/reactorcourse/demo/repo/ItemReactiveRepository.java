package com.reactorcourse.demo.repo;

import com.reactorcourse.demo.document.Item;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ItemReactiveRepository extends ReactiveMongoRepository<Item, String> {

}
