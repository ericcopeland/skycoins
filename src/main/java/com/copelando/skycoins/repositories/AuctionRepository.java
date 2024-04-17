package com.copelando.skycoins.repositories;

import com.copelando.skycoins.models.Auction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface AuctionRepository extends JpaRepository<Auction, UUID> {
    @Query("""
        select a
        from Auction a
        where (:itemId is null or a.decodedItemBytes.itemId = :itemId)
            and (:itemName is null or a.decodedItemBytes.name = :itemName)
            and (:itemNameLike is null or a.decodedItemBytes.name ilike %:itemNameLike%)
            and (:count is null or a.decodedItemBytes.count = :count)
            and (:minCount is null or a.decodedItemBytes.count >= :minCount)
            and (:maxCount is null or a.decodedItemBytes.count <= :maxCount)
            and (:damage is null or a.decodedItemBytes.damage = :damage)
            and (:minDamage is null or a.decodedItemBytes.damage >= :minDamage)
            and (:maxDamage is null or a.decodedItemBytes.damage <= :maxDamage)
            and (:rarity is null or a.decodedItemBytes.rarity ilike :rarity)
            and (:petLevel is null or a.decodedItemBytes.petLevel = :petLevel)
            and (:minPetLevel is null or a.decodedItemBytes.petLevel >= :minPetLevel)
            and (:maxPetLevel is null or a.decodedItemBytes.petLevel <= :maxPetLevel)
            and (:tiers is null or a.decodedItemBytes.rarity in :tiers)
    """)
    List<Auction> findAuctions(
            String itemId,
            String itemName,
            String itemNameLike,
            Integer count,
            Integer minCount,
            Integer maxCount,
            Integer damage,
            Integer minDamage,
            Integer maxDamage,
            String rarity,
            Integer petLevel,
            Integer minPetLevel,
            Integer maxPetLevel,
            String[] tiers,
            Pageable page
    );
}
