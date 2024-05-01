package com.copelando.skycoins.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.List;

@Entity
@Getter
@Setter
public class Product {
    @Id
    private String id;

    @CreationTimestamp
    private Instant createDate;

    @UpdateTimestamp
    private Instant lastUpdated;

    private String name;

    @ManyToOne
    private Item item;

    @OneToMany(mappedBy = "product")
    @JsonIgnore
    private List<ProductEntry> productEntries;

    @OneToOne
    private ProductEntry latestProductEntry;

    @OneToOne
    private ProductEntry referenceProductEntry;

    private long views;
}
