package com.reactorcourse.demo.initializer;

import com.reactorcourse.demo.document.Item;
import com.reactorcourse.demo.repo.ItemReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class ItemInitializer implements CommandLineRunner {

    private static final List<Item> items = List.of(
            new Item(null, "Mac Book Pro", 1233.5),
            new Item(null, "IPhone", 1330.4),
            new Item(null, "Tesla", 66355.65),
            new Item("123", "Biba", 65.65)
    );

    public ItemInitializer(ItemReactiveRepository itemRepo) {
        this.itemRepo = itemRepo;
    }

    private final ItemReactiveRepository itemRepo;

    @Override
    public void run(String... args) throws Exception {
        itemRepo.saveAll(items)
                .doOnNext(item -> log.info("Item: {} is inserted by initializer.", item))
                .subscribe();
    }
}
