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
public class Auction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @CreationTimestamp
    private Instant createDate;

    private UUID auctionId;

    private UUID seller;

    private UUID sellerProfile;

    private UUID buyer;

    private Instant timestamp;

    private double price;

    private boolean bin;

    @OneToOne(cascade = CascadeType.ALL)
    private AuctionItemBytes decodedItemBytes;

    @ManyToOne
    private Item item;
}
