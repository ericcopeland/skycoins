package com.copelando.skycoins.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(indexes = {
    @Index(columnList = "createDate", name = "product_entry_create_date")
})
public class ProductEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @CreationTimestamp
    private Instant createDate;

    private double buyPrice;

    private double sellPrice;

    private long buyVolume;

    private long sellVolume;

    private long buyOrders;

    private long sellOrders;

    private long buyMovingWeek;

    private long sellMovingWeek;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonIgnore
    private Product product;
}
