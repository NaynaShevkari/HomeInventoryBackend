package com.homeinventory.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "shopping_items")
public class ShoppingItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID shoppingItemId;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @Column(nullable = false)
    private String itemName;

    private double quantity;  // How much to buy

    private String unit;

    private LocalDateTime addedAt; // Timestamp when added
}
