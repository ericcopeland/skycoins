package com.copelando.skycoins.controllers;

import com.copelando.skycoins.models.Favorite;
import com.copelando.skycoins.models.Product;
import com.copelando.skycoins.repositories.FavoriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {
    private final FavoriteRepository favoriteRepository;

    @Autowired
    public FavoriteController(FavoriteRepository favoriteRepository) {
        this.favoriteRepository = favoriteRepository;
    }

    @PostMapping("/{username}")
    public Favorite postFavorite(@RequestBody Favorite favorite) {
        return favoriteRepository.save(favorite);
    }

    @GetMapping("/{username}/products")
    public List<Product> getProducts(@PathVariable String username) {
        return favoriteRepository.findProducts(username);
    }

    @GetMapping("/{username}/auctions")
    public List<Product> getAuctions(@PathVariable String username) {
        return favoriteRepository.findAuctions(username);
    }
}
