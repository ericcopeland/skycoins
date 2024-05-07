package com.copelando.skycoins.repositories;

import com.copelando.skycoins.models.ProductEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public interface ProductEntryRepository extends JpaRepository<ProductEntry, UUID> {
    @Query("""
        select pe
        from ProductEntry pe
        where pe.product.id = :productId
            and (:fromDate is null or pe.createDate >= :fromDate)
            and (:toDate is null or pe.createDate <= :toDate)
        order by pe.createDate asc
    """)
    List<ProductEntry> findProductEntries(
        String productId,
        Instant fromDate,
        Instant toDate
    );

    List<ProductEntry> findAllByProductIdOrderByCreateDateAsc(String productId);

    List<ProductEntry> findAllByProductIdAndCreateDateGreaterThanEqualOrderByCreateDateAsc(String productId, Instant fromDate);

    List<ProductEntry> findAllByProductIdAndCreateDateLessThanEqualOrderByCreateDateAsc(String productId, Instant toDate);

    List<ProductEntry> findAllByProductIdAndCreateDateGreaterThanEqualAndCreateDateLessThanEqualOrderByCreateDateAsc(String productId, Instant fromDate, Instant toDate);

}
