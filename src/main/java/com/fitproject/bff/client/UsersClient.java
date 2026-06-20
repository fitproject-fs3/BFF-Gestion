package com.fitproject.bff.client;

import com.fitproject.bff.dto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(name = "ms-users", url = "${services.ms-users.url:http://localhost:8090/api/v1}")
public interface UsersClient {

    @PostMapping("/users/authenticate")
    AuthResponse authenticateUser(@RequestBody AuthRequest request);

    @GetMapping("/users")
    List<UserDTO> getAllUsers();

    @PostMapping("/users")
    UserDTO createUser(@RequestBody CreateUserRequest request);

    @PatchMapping("/users/{userId}/role")
    UserDTO updateUserRole(@PathVariable("userId") String userId,
                           @RequestParam("role") String role);

    @PatchMapping("/users/{userId}/toggle")
    UserDTO toggleUserActive(@PathVariable("userId") String userId);

    @PostMapping("/users/validate-password")
    ResponseEntity<Void> validatePassword(@RequestBody Map<String, String> body);
}
