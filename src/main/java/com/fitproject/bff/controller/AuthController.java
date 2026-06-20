package com.fitproject.bff.controller;

import com.fitproject.bff.client.UsersClient;
import com.fitproject.bff.dto.*;
import com.fitproject.bff.security.JwtUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
@Tag(name = "Autenticación", description = "Login y registro de usuarios")
public class AuthController {

    private final UsersClient usersClient;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        log.info("Login attempt for: {}", request.getEmail());
        try {
            AuthResponse user = usersClient.authenticateUser(
                new AuthRequest(request.getEmail(), request.getPassword())
            );

            if (!Boolean.TRUE.equals(user.getActive())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Usuario inactivo. Contacta al administrador.");
            }

            String token = jwtUtil.generate(
                user.getUserId(), user.getEmail(), user.getRole(), user.getFullName()
            );

            return ResponseEntity.ok(LoginResponse.builder()
                .token(token)
                .userId(user.getUserId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(user.getRole())
                .build());

        } catch (Exception e) {
            log.warn("Login failed for {}: {}", request.getEmail(), e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Credenciales inválidas");
        }
    }
}