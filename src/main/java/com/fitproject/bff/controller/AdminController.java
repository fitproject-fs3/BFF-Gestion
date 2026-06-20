package com.fitproject.bff.controller;

import com.fitproject.bff.client.UsersClient;
import com.fitproject.bff.dto.CreateUserRequest;
import com.fitproject.bff.dto.UserDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
@Tag(name = "Admin", description = "Gestión de usuarios (solo ADMIN)")
public class AdminController {

    private final UsersClient usersClient;

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getUsers() {
        return ResponseEntity.ok(usersClient.getAllUsers());
    }

    @PostMapping("/users")
    public ResponseEntity<UserDTO> createUser(@RequestBody CreateUserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(usersClient.createUser(request));
    }

    @PatchMapping("/users/{userId}/role")
    public ResponseEntity<UserDTO> updateRole(
            @PathVariable String userId,
            @RequestParam String role) {
        return ResponseEntity.ok(usersClient.updateUserRole(userId, role));
    }

    @PatchMapping("/users/{userId}/toggle")
    public ResponseEntity<UserDTO> toggleActive(@PathVariable String userId) {
        return ResponseEntity.ok(usersClient.toggleUserActive(userId));
    }
}