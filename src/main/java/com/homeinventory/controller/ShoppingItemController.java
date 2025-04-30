package com.homeinventory.controller;

import com.homeinventory.model.ShoppingItem;
import com.homeinventory.service.ShoppingItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/shopping")
public class ShoppingItemController {

    @Autowired
    private ShoppingItemService shoppingItemService;

    @PostMapping("/add")
    public ResponseEntity<ShoppingItem> addShoppingItem(@RequestParam String groupName,
                                                        @RequestParam String itemName,
                                                        @RequestParam int quantity) {
        ShoppingItem item = shoppingItemService.addShoppingItem(groupName, itemName, quantity);
        return ResponseEntity.ok(item);
    }

    @GetMapping("/{groupName}")
    public ResponseEntity<List<ShoppingItem>> getShoppingList(@PathVariable String groupName) {
        List<ShoppingItem> items = shoppingItemService.getShoppingList(groupName);
        return ResponseEntity.ok(items);
    }

    @DeleteMapping("/{shoppingItemId}")
    public ResponseEntity<String> removeShoppingItem(@PathVariable UUID shoppingItemId) {
        shoppingItemService.removeShoppingItem(shoppingItemId);
        return ResponseEntity.ok("Item removed from shopping list successfully!");
    }
}
