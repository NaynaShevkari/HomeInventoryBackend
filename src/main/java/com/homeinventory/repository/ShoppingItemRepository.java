package com.homeinventory.repository;

import com.homeinventory.model.Group;
import com.homeinventory.model.ShoppingItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ShoppingItemRepository extends JpaRepository<ShoppingItem, UUID> {

    List<ShoppingItem> findByGroup(Group group);

}
