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

/**
 * Controller REST del BFF-Gestion para el flujo de autenticación centralizado.
 *
 * <p>Recibe las credenciales del frontend, las valida contra MS-Users vía Feign,
 * y emite un JWT firmado con el rol del usuario. El frontend redirige al portal
 * correspondiente según el rol recibido en la respuesta.</p>
 *
 * <p>Base URL: {@code /auth}</p>
 *
 * @see JwtUtil
 * @see com.fitproject.bff.client.UsersClient
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
@Tag(name = "Autenticación", description = "Login y registro de usuarios")
public class AuthController {

    private final UsersClient usersClient;
    private final JwtUtil jwtUtil;

    /**
     * Autentica al usuario y emite un token JWT firmado con su rol y datos de perfil.
     *
     * <p>Flujo: valida credenciales en MS-Users → verifica estado activo → genera JWT
     * con {@code userId}, {@code email}, {@code role} y {@code fullName} como claims.</p>
     *
     * @param request DTO con {@code email} y {@code password}
     * @return 200 con {@link LoginResponse} que incluye token y datos del usuario;
     *         401 si las credenciales son inválidas; 403 si el usuario está inactivo
     */
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