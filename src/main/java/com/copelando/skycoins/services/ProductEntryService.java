package com.copelando.skycoins.services;

import com.copelando.skycoins.models.ProductEntry;
import com.copelando.skycoins.repositories.ProductEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class ProductEntryService {
    private final ProductEntryRepository productEntryRepository;

    @Autowired
    public ProductEntryService(ProductEntryRepository productEntryRepository) {
        this.productEntryRepository = productEntryRepository;
    }

    public List<ProductEntry> getProductEntries(
        String productId,
        Optional<Instant> fromDate,
        Optional<Instant> toDate
    ) {
        if (fromDate.isEmpty() && toDate.isEmpty()) {
            return productEntryRepository.findAllByProductIdOrderByCreateDateAsc(productId);
        }
        if (fromDate.isEmpty()) {
            return productEntryRepository.findAllByProductIdAndCreateDateLessThanEqualOrderByCreateDateAsc(productId, toDate.get());
        }
        if (toDate.isEmpty()) {
            return productEntryRepository.findAllByProductIdAndCreateDateGreaterThanEqualOrderByCreateDateAsc(productId, fromDate.get());
        }
        return productEntryRepository.findAllByProductIdAndCreateDateGreaterThanEqualAndCreateDateLessThanEqualOrderByCreateDateAsc(productId, fromDate.get(), toDate.get());
    }
}
