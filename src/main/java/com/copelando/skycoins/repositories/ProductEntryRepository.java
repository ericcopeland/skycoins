package com.copelando.skycoins.repositories;

import com.copelando.skycoins.models.ProductEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductEntryRepository extends JpaRepository<ProductEntry, UUID> {
}
