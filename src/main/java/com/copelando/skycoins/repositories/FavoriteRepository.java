package com.copelando.skycoins.repositories;

import com.copelando.skycoins.models.Favorite;
import com.copelando.skycoins.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, UUID> {
    @Query("""
        select f.product
        from Favorite f
        where f.username = :username
            and f.product is not null
    """)
    List<Product> findProducts(String username);

    @Query("""
        select f.auction
        from Favorite f
        where f.username = :username
            and f.auction is not null
    """)
    List<Product> findAuctions(String username);
}
