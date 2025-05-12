package com.homeinventory.service;

import com.homeinventory.model.Group;
import com.homeinventory.model.InventoryItem;
import com.homeinventory.model.ShoppingItem;
import com.homeinventory.repository.GroupRepository;
import com.homeinventory.repository.InventoryRepository;
import com.homeinventory.repository.ShoppingItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ShoppingItemService {

    @Autowired
    private ShoppingItemRepository shoppingItemRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private GroupRepository groupRepository;

    public ShoppingItem addShoppingItem(String groupName, String itemName, int quantity, String unit) {
        Group group = groupRepository.findByGroupName(groupName)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        Optional<ShoppingItem> existingOpt = shoppingItemRepository.findByGroupAndItemNameAndUnit(group, itemName, unit);
        if (existingOpt.isPresent()) {
            ShoppingItem existing = existingOpt.get();
            existing.setQuantity(existing.getQuantity() + quantity);
            return shoppingItemRepository.save(existing);
        }

        ShoppingItem item = new ShoppingItem();
        item.setGroup(group);
        item.setItemName(itemName);
        item.setQuantity(quantity);
        item.setUnit(unit);
        item.setAddedAt(LocalDateTime.now());
        return shoppingItemRepository.save(item);
    }

    public List<ShoppingItem> getShoppingList(String groupName) {
        Group group = groupRepository.findByGroupName(groupName)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        return shoppingItemRepository.findByGroup(group);
    }

    public void removeShoppingItem(UUID shoppingItemId) {
        shoppingItemRepository.deleteById(shoppingItemId);
    }

    public ShoppingItem updateItem(UUID itemId, ShoppingItem updatedItem) {
        ShoppingItem existing = shoppingItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));
        existing.setItemName(updatedItem.getItemName());
        existing.setQuantity(updatedItem.getQuantity());
        return shoppingItemRepository.save(existing);
    }

    public void deleteItem(UUID itemId) {
        if (!shoppingItemRepository.existsById(itemId)) {
            throw new RuntimeException("Item not found");
        }
        shoppingItemRepository.deleteById(itemId);
    }

    public void markAsBought(UUID itemId) {
        ShoppingItem item = shoppingItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        InventoryItem inventoryItem = new InventoryItem();
        inventoryItem.setGroup(item.getGroup());
        inventoryItem.setItemName(item.getItemName());
        inventoryItem.setQuantity(item.getQuantity());
        inventoryItem.setUnit(item.getUnit()); // optional: default or add in ShoppingItem model
//        inventoryItem.setAddedBy("system"); // or pull from session if needed
//        inventoryItem.setAddedBy(item.getUser());

        inventoryRepository.save(inventoryItem);
        shoppingItemRepository.deleteById(itemId);
    }

}
