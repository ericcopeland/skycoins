package com.copelando.skycoins.services;

import com.copelando.skycoins.models.Product;
import com.copelando.skycoins.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product getProduct(String productId) {
        return productRepository.findById(productId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public List<Product> getTrendingProducts() {
        return productRepository.getTrendingProducts();
    }

    public List<Product> getMostViewedProducts() {
        return productRepository.getMostViewedProducts();
    }

    public List<Product> getRecentlyAddedProducts() {
        return productRepository.getRecentlyAddedProducts();
    }
}
