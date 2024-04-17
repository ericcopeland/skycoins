package com.copelando.skycoins.controllers;

import com.copelando.skycoins.models.ProductEntry;
import com.copelando.skycoins.repositories.ProductEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/product-entries")
public class ProductEntryController {
    private final ProductEntryRepository productEntryRepository;

    @Autowired
    public ProductEntryController(
            ProductEntryRepository productEntryRepository
    ) {
        this.productEntryRepository = productEntryRepository;
    }

    @GetMapping
    List<ProductEntry> getAllProductEntries() {
        return productEntryRepository.findAll();
    }
}
