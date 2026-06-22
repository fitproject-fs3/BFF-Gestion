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

/**
 * Controller REST del BFF-Gestion para operaciones administrativas de usuarios.
 *
 * <p>Actúa como proxy hacia MS-Users para las operaciones que requieren rol {@code ADMIN}.
 * Protegido por el filtro JWT del BFF; el Circuit Breaker activado en
 * {@link com.fitproject.bff.client.UsersClient} garantiza degradación segura
 * si MS-Users no está disponible.</p>
 *
 * <p>Base URL: {@code /api/v1/admin}</p>
 *
 * @see com.fitproject.bff.client.UsersClient
 */
@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
@Tag(name = "Admin", description = "Gestión de usuarios (solo ADMIN)")
public class AdminController {

    private final UsersClient usersClient;

    /**
     * Retorna todos los usuarios del sistema para la vista de gestión del ADMIN.
     *
     * @return 200 con lista de usuarios; lista vacía si MS-Users no responde (fallback)
     */
    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getUsers() {
        return ResponseEntity.ok(usersClient.getAllUsers());
    }

    /**
     * Crea un nuevo usuario en MS-Users desde el panel de administración.
     *
     * @param request datos del nuevo usuario: email, contraseña, nombre completo y rol
     * @return 201 Created con el DTO del usuario persistido
     */
    @PostMapping("/users")
    public ResponseEntity<UserDTO> createUser(@RequestBody CreateUserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(usersClient.createUser(request));
    }

    /**
     * Actualiza el rol de un usuario existente.
     *
     * @param userId identificador UUID del usuario a modificar
     * @param role   nuevo rol: {@code ADMIN}, {@code SUPERVISOR_OBRA}, {@code TRABAJADOR}, etc.
     * @return 200 con el DTO del usuario actualizado
     */
    @PatchMapping("/users/{userId}/role")
    public ResponseEntity<UserDTO> updateRole(
            @PathVariable String userId,
            @RequestParam String role) {
        return ResponseEntity.ok(usersClient.updateUserRole(userId, role));
    }

    /**
     * Alterna el estado activo/inactivo de un usuario (soft enable/disable).
     *
     * @param userId identificador UUID del usuario
     * @return 200 con el DTO del usuario con el nuevo estado {@code active}
     */
    @PatchMapping("/users/{userId}/toggle")
    public ResponseEntity<UserDTO> toggleActive(@PathVariable String userId) {
        return ResponseEntity.ok(usersClient.toggleUserActive(userId));
    }
}