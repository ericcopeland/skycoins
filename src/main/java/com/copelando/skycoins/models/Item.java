package com.copelando.skycoins.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Getter
@Setter
@Entity
public class Item {
    @Id
    private String id;

    @CreationTimestamp
    private Instant createDate;

    private String material;

    private String name;

    private String category;

    private String tier;

    private long npcSellPrice;

    @Column(columnDefinition = "TEXT")
    private String skin;
}
