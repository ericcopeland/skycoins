package com.copelando.skycoins.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
public class AuctionItemBytes {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @CreationTimestamp
    private Instant createDate;

    private String itemId;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String lore;

    private int count;

    private int damage;

    private String rarity;

    private int petLevel;
}
