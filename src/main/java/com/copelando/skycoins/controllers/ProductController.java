package com.copelando.skycoins.controllers;

import com.copelando.skycoins.models.Product;
import com.copelando.skycoins.models.ProductEntry;
import com.copelando.skycoins.repositories.ProductRepository;
import com.copelando.skycoins.services.ProductEntryService;
import com.copelando.skycoins.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductRepository productRepository;
    private final ProductService productService;
    private final ProductEntryService productEntryService;

    @Autowired
    public ProductController(
            ProductRepository productRepository,
            ProductService productService,
            ProductEntryService productEntryService
    ) {
        this.productRepository = productRepository;
        this.productService = productService;
        this.productEntryService = productEntryService;
    }

    @GetMapping
    List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @GetMapping("/{id}")
    Product getProduct(@PathVariable String id) {
        return productService.getProduct(id);
    }

    @GetMapping("/{id}/product-entries")
    List<ProductEntry> getProductEntries(
            @PathVariable String id,
            @RequestParam Optional<Instant> fromDate,
            @RequestParam Optional<Instant> toDate
    ) {
        return productEntryService.getProductEntries(id, fromDate, toDate);
    }

    @GetMapping("/trending")
    List<Product> getTrendingProducts() {
        return productService.getTrendingProducts();
    }

    @GetMapping("/most-viewed")
    List<Product> getMostViewedProducts() {
        return productService.getMostViewedProducts();
    }

    @GetMapping("/recently-added")
    List<Product> getRecentlyAddedProducts() {
        return productService.getRecentlyAddedProducts();
    }

    @GetMapping("/count")
    long getCount() {
        return productRepository.count();
    }

    @PutMapping("/{id}/views")
    Product putProductViews(@PathVariable String id) {
        var product = productService.getProduct(id);
        product.setViews(product.getViews() + 1);
        return productRepository.save(product);
    }
}
