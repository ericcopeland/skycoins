package com.copelando.skycoins.repositories;

import com.copelando.skycoins.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    @Query("""
        select p
        from Product p
        where p.referenceProductEntry.sellPrice != 0
        order by (p.latestProductEntry.sellPrice - p.referenceProductEntry.sellPrice) / p.referenceProductEntry.sellPrice desc
        limit 10
    """)
    List<Product> getTrendingProducts();

    @Query("""
        select p
        from Product p
        order by p.views desc
        limit 10
    """)
    List<Product> getMostViewedProducts();

    @Query("""
        select p
        from Product p
        order by p.createDate desc
        limit 10
    """)
    List<Product> getRecentlyAddedProducts();
}
