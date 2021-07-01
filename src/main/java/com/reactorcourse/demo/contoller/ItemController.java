package com.reactorcourse.demo.contoller;

import com.reactorcourse.demo.document.Item;
import com.reactorcourse.demo.repo.ItemReactiveRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class ItemController {
    private final ItemReactiveRepository itemRepo;

    public ItemController(ItemReactiveRepository itemRepo) {
        this.itemRepo = itemRepo;
    }

    @GetMapping("/item")
    public Flux<Item> getItems() {
        return itemRepo.findAll();
    }

    @GetMapping("/item/{id}")
    public Mono<ResponseEntity<Item>> getItems(@PathVariable String id) {
        return itemRepo.findById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping("/item")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Item> save(@RequestBody Item item) {
        return itemRepo.save(item);
    }

    @DeleteMapping("/item/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable String id) {
        return itemRepo.deleteById(id);
    }
}
