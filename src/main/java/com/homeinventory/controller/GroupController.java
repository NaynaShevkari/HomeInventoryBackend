package com.homeinventory.controller;

import com.homeinventory.model.Group;
import com.homeinventory.model.User;
import com.homeinventory.model.UserGroupMembership;
import com.homeinventory.repository.GroupRepository;
import com.homeinventory.repository.UserGroupMembershipRepository;
import com.homeinventory.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/groups")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserGroupMembershipRepository membershipRepository;

    @PostMapping("/create")
    public ResponseEntity<Group> createGroup(@RequestParam String groupName, @RequestParam String username) {
        return ResponseEntity.ok(groupService.createGroup(groupName, username));
    }


    @GetMapping("/{groupName}")
    public ResponseEntity<Group> getGroupByName(@PathVariable String groupName) {
        return groupService.getGroupByName(groupName)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/id/{groupId}")
    public ResponseEntity<Group> getGroupById(@PathVariable UUID groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));
        return ResponseEntity.ok(group);
    }

    @GetMapping("/{groupId}/members")
    public ResponseEntity<List<User>> getApprovedUsers(@PathVariable UUID groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        List<UserGroupMembership> approvedMemberships =
                membershipRepository.findByGroupAndIsApprovedTrue(group);

        List<User> approvedUsers = approvedMemberships.stream()
                .map(UserGroupMembership::getUser)
                .toList();

        return ResponseEntity.ok(approvedUsers);
    }


    @GetMapping("/{groupId}/pending-members")
    public ResponseEntity<List<UserGroupMembership>> getPendingMembers(@PathVariable UUID groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        List<UserGroupMembership> pending = membershipRepository.findByGroupAndIsApprovedFalse(group);
        return ResponseEntity.ok(pending);
    }

    @DeleteMapping("/{groupId}/exit")
    public ResponseEntity<String> exitGroup(@PathVariable UUID groupId, @RequestParam String username) {
        groupService.exitGroup(groupId, username);
        return ResponseEntity.ok("User exited the group successfully.");
    }

}
