package com.homeinventory.service;

import com.homeinventory.model.Group;
import com.homeinventory.model.User;
import com.homeinventory.model.UserGroupMembership;
import com.homeinventory.repository.GroupRepository;
import com.homeinventory.repository.UserGroupMembershipRepository;
import com.homeinventory.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserGroupMembershipRepository membershipRepository;

    public void joinGroup(String username, String groupName) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Group group = groupRepository.findByGroupName(groupName)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        if (membershipRepository.existsByGroupAndUser(group, user)) {
            throw new RuntimeException("User already requested to join or is a member of this group.");
        }

        UserGroupMembership membership = new UserGroupMembership();
        membership.setUser(user);
        membership.setGroup(group);
        membership.setApproved(false); // Pending initially

        membershipRepository.save(membership);
    }

    public void approveMembership(UUID membershipId) {
        UserGroupMembership membership = membershipRepository.findById(membershipId)
                .orElseThrow(() -> new RuntimeException("Membership not found"));

        membership.setApproved(true);
        membershipRepository.save(membership);
    }


    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void approveUser(UUID membershipId) {
        UserGroupMembership membership = membershipRepository.findById(membershipId)
                .orElseThrow(() -> new RuntimeException("Membership not found"));

        membership.setApproved(true);
        membershipRepository.save(membership);
    }


//    public List<User> getPendingUsers(String groupName) {
//        Group group = groupRepository.findByGroupName(groupName)
//                .orElseThrow(() -> new RuntimeException("Group not found"));
//        return userRepository.findByGroupAndIsApprovedFalse(group);
//    }

    public List<User> getPendingUsers(String groupName) {
        Group group = groupRepository.findByGroupName(groupName)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        List<UserGroupMembership> pendingMemberships = membershipRepository.findByGroupAndIsApprovedFalse(group);

        List<User> pendingUsers = pendingMemberships.stream()
                .map(UserGroupMembership::getUser)
                .toList();

        return pendingUsers;
    }

    public List<Group> getGroupsForUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<UserGroupMembership> memberships = membershipRepository.findByUserAndIsApprovedTrue(user);
        return memberships.stream()
                .map(UserGroupMembership::getGroup)
                .toList();
    }


}
