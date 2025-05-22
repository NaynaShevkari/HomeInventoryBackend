package com.homeinventory.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "inventory_items")
public class InventoryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID itemId;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @Column(nullable = false)
    private String itemName;

    private double quantity;  // Number of units available

    private String unit;   // Kg, Litre, Packets, etc.

    private LocalDate expiryDate; // Optional

    @ManyToOne
    @JoinColumn(name = "added_by_user_id")
    private User addedBy;   // Which user added this item

    private LocalDateTime addedAt; // Timestamp of addition

    private String store;

    private String category;
}
