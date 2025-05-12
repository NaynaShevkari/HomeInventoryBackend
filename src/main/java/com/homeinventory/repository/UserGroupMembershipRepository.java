package com.homeinventory.repository;

import com.homeinventory.model.Group;
import com.homeinventory.model.User;
import com.homeinventory.model.UserGroupMembership;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserGroupMembershipRepository extends JpaRepository<UserGroupMembership, UUID> {

    List<UserGroupMembership> findByUser(User user);

    List<UserGroupMembership> findByGroupAndIsApprovedFalse(Group group);

//    List<UserGroupMembership> findByGroupAndApprovedFalse(Group group);

    List<UserGroupMembership> findByUserAndIsApprovedTrue(User user);

    List<UserGroupMembership> findByGroupAndIsApprovedTrue(Group group);

    boolean existsByGroupAndUser(Group group, User user);

    Optional<UserGroupMembership> findByUserAndGroup(User user, Group group);

}
