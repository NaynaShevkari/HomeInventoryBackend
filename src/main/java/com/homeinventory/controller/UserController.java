package com.homeinventory.controller;

import com.homeinventory.model.Group;
import com.homeinventory.model.User;
import com.homeinventory.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/join")
    public ResponseEntity<?> joinGroup(@RequestParam String username, @RequestParam String groupName) {
        try {
            userService.joinGroup(username, groupName);
            return ResponseEntity.ok("Join request sent successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/approve/{membershipId}")
    public ResponseEntity<?> approveUser(@PathVariable UUID membershipId) {
        try {
            userService.approveMembership(membershipId);
            return ResponseEntity.ok("User approved successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/pending/{groupName}")
    public ResponseEntity<List<User>> getPendingUsers(@PathVariable String groupName) {
        List<User> pendingUsers = userService.getPendingUsers(groupName);
        return ResponseEntity.ok(pendingUsers);
    }

    @GetMapping("/groups/{username}")
    public ResponseEntity<List<Group>> getGroupsForUser(@PathVariable String username) {
        return ResponseEntity.ok(userService.getGroupsForUser(username));
    }


}
