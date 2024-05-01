package com.copelando.skycoins.controllers;

import com.copelando.skycoins.models.Auction;
import com.copelando.skycoins.repositories.AuctionRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/auctions")
public class AuctionController {
    private final AuctionRepository auctionRepository;

    public AuctionController(AuctionRepository auctionRepository) {
        this.auctionRepository = auctionRepository;
    }

    @GetMapping("/{id}")
    public Auction getAuction(@PathVariable UUID id) {
        return auctionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public List<Auction> getAuctions(
            @RequestParam Optional<String> itemId,
            @RequestParam Optional<String> itemName,
            @RequestParam Optional<String> itemNameLike,
            @RequestParam Optional<Integer> count,
            @RequestParam Optional<Integer> minCount,
            @RequestParam Optional<Integer> maxCount,
            @RequestParam Optional<Integer> damage,
            @RequestParam Optional<Integer> minDamage,
            @RequestParam Optional<Integer> maxDamage,
            @RequestParam Optional<String> rarity,
            @RequestParam Optional<Integer> petLevel,
            @RequestParam Optional<Integer> minPetLevel,
            @RequestParam Optional<Integer> maxPetLevel,
            @RequestParam Optional<String[]> tiers,
            @RequestParam Optional<Integer> limit
    ) {
        return auctionRepository.findAuctions(
                itemId.orElse(null),
                itemName.orElse(null),
                itemNameLike.orElse(null),
                count.orElse(null),
                minCount.orElse(null),
                maxCount.orElse(null),
                damage.orElse(null),
                minDamage.orElse(null),
                maxDamage.orElse(null),
                rarity.orElse(null),
                petLevel.orElse(null),
                minPetLevel.orElse(null),
                maxPetLevel.orElse(null),
                tiers.orElse(null),
                limit.map(l -> PageRequest.of(0, l)).orElse(null)
        );
    }

    @GetMapping("/count")
    public long getCount() {
        return auctionRepository.count();
    }
}
