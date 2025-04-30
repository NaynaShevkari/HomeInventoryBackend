package com.homeinventory.controller;

import com.homeinventory.model.InventoryItem;
import com.homeinventory.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @PostMapping("/add")
    public ResponseEntity<InventoryItem> addItem(@RequestParam String groupName,
                                                 @RequestParam String username,
                                                 @RequestParam String itemName,
                                                 @RequestParam int quantity,
                                                 @RequestParam String unit,
                                                 @RequestParam(required = false)
                                                 @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                 LocalDate expiryDate) {
        InventoryItem item = inventoryService.addItem(groupName, username, itemName, quantity, unit, expiryDate);
        return ResponseEntity.ok(item);
    }

    @GetMapping("/{groupName}")
    public ResponseEntity<List<InventoryItem>> getInventory(@PathVariable String groupName) {
        List<InventoryItem> inventoryItems = inventoryService.getGroupInventory(groupName);
        return ResponseEntity.ok(inventoryItems);
    }

    @PutMapping("/reduce/{itemId}")
    public ResponseEntity<?> reduceQuantity(@PathVariable UUID itemId,
                                            @RequestParam int quantity) {
        try {
            InventoryItem updatedItem = inventoryService.updateQuantity(itemId, quantity);
            return ResponseEntity.ok(updatedItem);
        } catch (RuntimeException e) {
            return ResponseEntity.ok(e.getMessage());
        }
    }

}
