package com.copelando.skycoins.controllers;

import com.copelando.skycoins.models.Item;
import com.copelando.skycoins.repositories.ItemRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/items")
public class ItemController {
    private final ItemRepository itemRepository;

    public ItemController(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @GetMapping
    public List<Item> getItems() {
        return itemRepository.findAll();
    }

    @GetMapping("/{id}")
    public Item getItem(@PathVariable String id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/count")
    public long getCount() {
        return itemRepository.count();
    }
}
