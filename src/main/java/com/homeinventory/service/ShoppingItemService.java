package com.homeinventory.service;

import com.homeinventory.model.Group;
import com.homeinventory.model.ShoppingItem;
import com.homeinventory.repository.GroupRepository;
import com.homeinventory.repository.ShoppingItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ShoppingItemService {

    @Autowired
    private ShoppingItemRepository shoppingItemRepository;

    @Autowired
    private GroupRepository groupRepository;

    public ShoppingItem addShoppingItem(String groupName, String itemName, int quantity) {
        Group group = groupRepository.findByGroupName(groupName)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        ShoppingItem item = new ShoppingItem();
        item.setGroup(group);
        item.setItemName(itemName);
        item.setQuantity(quantity);
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
}
