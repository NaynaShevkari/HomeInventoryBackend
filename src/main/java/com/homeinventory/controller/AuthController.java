package com.homeinventory.controller;

import com.homeinventory.model.User;
import com.homeinventory.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestParam String username, @RequestParam String password, @RequestParam String displayName) {
        User user = new User();
//        user.setUserId(UUID.randomUUID());
        user.setUsername(username);
        user.setPassword(password);
        user.setDisplayName(displayName);
//        user.setApproved(false);
        userRepository.save(user);
        return ResponseEntity.ok("Signup successful!");
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password) {
        Optional<User> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isEmpty()) {
            return ResponseEntity.badRequest().body("Username not found.");
        }

        User user = optionalUser.get();

        if (!user.getPassword().equals(password)) {
            return ResponseEntity.badRequest().body("Incorrect password.");
        }

        return ResponseEntity.ok(user); // return user object
    }
}
