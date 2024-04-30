package com.copelando.skycoins.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String username;

    @OneToOne(cascade = CascadeType.ALL)
    private Product product;

    @OneToOne(cascade = CascadeType.ALL)
    private Auction auction;
}
