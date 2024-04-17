package com.copelando.skycoins.controllers;

import com.copelando.skycoins.models.Product;
import com.copelando.skycoins.models.ProductEntry;
import com.copelando.skycoins.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductRepository productRepository;

    @Autowired
    public ProductController(
            ProductRepository productRepository
    ) {
        this.productRepository = productRepository;
    }

    @GetMapping
    List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @GetMapping("/{id}")
    Product getProduct(@PathVariable String id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/{id}/product-entries")
    List<ProductEntry> getProductEntries(
            @PathVariable String id,
            @RequestParam Optional<Instant> fromDate,
            @RequestParam Optional<Instant> toDate
    ) {
        var productEntries = productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND))
                .getProductEntries().stream();

        if (fromDate.isPresent()) {
            productEntries = productEntries
                    .filter(productEntry -> productEntry.getCreateDate().isAfter(fromDate.get()));
        }

        if (toDate.isPresent()) {
            productEntries = productEntries
                    .filter(productEntry -> productEntry.getCreateDate().isBefore(toDate.get()));
        }

        return productEntries.toList();
    }

    @GetMapping("/count")
    long getCount() {
        return productRepository.count();
    }
}
