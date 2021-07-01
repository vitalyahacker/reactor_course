package com.reactorcourse.demo.contoller;

import com.reactorcourse.demo.document.Item;
import com.reactorcourse.demo.repo.ItemReactiveRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class ItemController {
    private final ItemReactiveRepository itemRepo;

    public ItemController(ItemReactiveRepository itemRepo) {
        this.itemRepo = itemRepo;
    }

    @GetMapping("/items")
    public Flux<Item> getItems() {
        return itemRepo.findAll();
    }

    @PostMapping("/item")
    public Mono<Item> save(Item item) {
        return itemRepo.save(item);
    }
}
