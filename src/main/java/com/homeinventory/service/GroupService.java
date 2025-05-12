package com.homeinventory.service;

import com.homeinventory.model.Group;
import com.homeinventory.model.User;
import com.homeinventory.model.UserGroupMembership;
import com.homeinventory.repository.GroupRepository;
import com.homeinventory.repository.UserGroupMembershipRepository;
import com.homeinventory.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class GroupService {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository; // Inject UserRepository

    @Autowired
    private UserGroupMembershipRepository membershipRepository;

    public Group createGroup(String groupName, String username) {
        Optional<Group> existingGroup = groupRepository.findByGroupName(groupName);
        if (existingGroup.isPresent()) {
            throw new RuntimeException("Group name already exists!");
        }

        User creator = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Creator user not found"));

        Group group = new Group();
        group.setGroupName(groupName);
        group.setCreatedBy(creator.getUserId().toString());
        Group savedGroup = groupRepository.save(group);

        // NEW: Insert membership for creator
        UserGroupMembership membership = new UserGroupMembership();
        membership.setUser(creator);
        membership.setGroup(savedGroup);
        membership.setApproved(true);
        membershipRepository.save(membership);

        return savedGroup;
    }
    public Optional<Group> getGroupByName(String groupName) {
        return groupRepository.findByGroupName(groupName);
    }

    public void exitGroup(UUID groupId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        UserGroupMembership membership = membershipRepository.findByUserAndGroup(user, group)
                .orElseThrow(() -> new RuntimeException("Membership not found"));

        membershipRepository.delete(membership);
    }
}
