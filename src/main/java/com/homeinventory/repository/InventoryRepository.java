package com.homeinventory.repository;

import com.homeinventory.model.InventoryItem;
import com.homeinventory.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface InventoryRepository extends JpaRepository<InventoryItem, UUID> {

    List<InventoryItem> findByGroup(Group group);

}
