package com.homeinventory.service;

import com.homeinventory.model.Group;
import com.homeinventory.model.InventoryItem;
import com.homeinventory.model.User;
import com.homeinventory.repository.GroupRepository;
import com.homeinventory.repository.InventoryRepository;
import com.homeinventory.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;

    public InventoryItem addItem(String groupName, String username, String itemName, int quantity, String unit, LocalDate expiryDate) {
        Group group = groupRepository.findByGroupName(groupName)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        InventoryItem item = new InventoryItem();
        item.setGroup(group);
        item.setItemName(itemName);
        item.setQuantity(quantity);
        item.setUnit(unit);
        item.setExpiryDate(expiryDate);
        item.setAddedBy(user);
        item.setAddedAt(LocalDateTime.now());

        return inventoryRepository.save(item);
    }

    public List<InventoryItem> getGroupInventory(String groupName) {
        Group group = groupRepository.findByGroupName(groupName)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        return inventoryRepository.findByGroup(group);
    }

    @Autowired
    private ShoppingItemService shoppingItemService; // Inject ShoppingItemService

    public InventoryItem updateQuantity(UUID itemId, int quantityToReduce) {
        InventoryItem item = inventoryRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        int updatedQuantity = item.getQuantity() - quantityToReduce;

        if (updatedQuantity <= 0) {
            // If quantity becomes zero or negative, remove from inventory and add to shopping list
            inventoryRepository.delete(item);

            // Add to shopping list
            shoppingItemService.addShoppingItem(
                    item.getGroup().getGroupName(),
                    item.getItemName(),
                    1 // default to 1 unit to buy
            );

            throw new RuntimeException("Item finished! Moved to shopping list.");
        } else {
            item.setQuantity(updatedQuantity);
            return inventoryRepository.save(item);
        }
    }

}
