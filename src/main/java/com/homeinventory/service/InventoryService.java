package com.homeinventory.service;

import com.homeinventory.model.Group;
import com.homeinventory.model.InventoryItem;
import com.homeinventory.model.ShoppingItem;
import com.homeinventory.model.User;
import com.homeinventory.repository.GroupRepository;
import com.homeinventory.repository.InventoryRepository;
import com.homeinventory.repository.ShoppingItemRepository;
import com.homeinventory.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

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

    @Autowired
    private ShoppingItemRepository shoppingItemRepository;


    public InventoryItem addItem(String groupName, String username, String itemName, double quantity, String unit, LocalDate expiryDate, String store, String category) {
        Group group = groupRepository.findByGroupName(groupName)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Optional<InventoryItem> existingItemOpt = inventoryRepository.findByGroupAndItemNameAndUnit(group, itemName, unit);
        if (existingItemOpt.isPresent()) {
            InventoryItem existingItem = existingItemOpt.get();
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            return inventoryRepository.save(existingItem);
        }

        InventoryItem item = new InventoryItem();
        item.setGroup(group);
        item.setItemName(itemName);
        item.setQuantity(quantity);
        item.setUnit(unit);
        item.setExpiryDate(expiryDate);
        item.setStore(store);
        item.setCategory(category);
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

        double updatedQuantity = item.getQuantity() - quantityToReduce;

        if (updatedQuantity <= 0) {
            // If quantity becomes zero or negative, remove from inventory and add to shopping list
            inventoryRepository.delete(item);

            // Add to shopping list
            shoppingItemService.addShoppingItem(
                    item.getGroup().getGroupName(),
                    item.getItemName(),
                    item.getQuantity(),
                    item.getUnit()// default to 1 unit to buy
            );

            throw new RuntimeException("Item finished! Moved to shopping list.");
        } else {
            item.setQuantity(updatedQuantity);
            return inventoryRepository.save(item);
        }
    }

    public void markAsFinished(UUID itemId, boolean addToShopping) {
        InventoryItem item = inventoryRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        if (addToShopping) {
            ShoppingItem shoppingItem = new ShoppingItem();
            shoppingItem.setGroup(item.getGroup());
            shoppingItem.setItemName(item.getItemName());
            shoppingItem.setQuantity(item.getQuantity());
            shoppingItem.setUnit(item.getUnit());
            shoppingItem.setAddedAt(LocalDateTime.now());
            shoppingItemRepository.save(shoppingItem);

        }

        inventoryRepository.delete(item);
    }

    public InventoryItem updateItem(UUID itemId, InventoryItem updatedItem) {
        InventoryItem existingItem = inventoryRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        existingItem.setItemName(updatedItem.getItemName());
        existingItem.setQuantity(updatedItem.getQuantity());
        existingItem.setUnit(updatedItem.getUnit());
        existingItem.setExpiryDate(updatedItem.getExpiryDate());

        return inventoryRepository.save(existingItem);
    }

    public void deleteItem(UUID itemId) {
        if (!inventoryRepository.existsById(itemId)) {
            throw new RuntimeException("Item not found");
        }
        inventoryRepository.deleteById(itemId);
    }


}
