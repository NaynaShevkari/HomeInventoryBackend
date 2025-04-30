package com.homeinventory.repository;

import com.homeinventory.model.Group;
import com.homeinventory.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByUsername(String username);

//    List<User> findByGroupAndIsApprovedFalse(Group group);


}
