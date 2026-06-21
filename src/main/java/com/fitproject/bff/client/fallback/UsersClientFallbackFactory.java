package com.fitproject.bff.client.fallback;

import com.fitproject.bff.client.UsersClient;
import com.fitproject.bff.dto.*;
import feign.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Fallback Factory para {@link UsersClient} del BFF-Gestion.
 *
 * <p>Gestiona la degradación cuando MS-Users no responde.
 * La autenticación lanza excepción deliberadamente para no generar tokens
 * silenciosos en caso de fallo real del servicio.</p>
 *
 * @see UsersClient
 */
@Slf4j
@Component
public class UsersClientFallbackFactory implements FallbackFactory<UsersClient> {

    /**
     * Crea una instancia fallback de {@link UsersClient} que registra el fallo
     * y aplica degradación segura por método.
     *
     * @param cause excepción que activó el circuit breaker
     * @return implementación de degradación de {@link UsersClient}
     */
    @Override
    public UsersClient create(Throwable cause) {
        log.error("[CircuitBreaker] MS-Users no disponible: {}", cause.getMessage());
        return new UsersClient() {

            @Override
            public AuthResponse authenticateUser(AuthRequest request) {
                // La autenticación no puede degradarse silenciosamente
                throw new IllegalStateException("Servicio de autenticación no disponible. Intenta de nuevo más tarde.");
            }

            @Override
            public List<UserDTO> getAllUsers() {
                log.warn("[Fallback] getAllUsers → lista vacía");
                return Collections.emptyList();
            }

            @Override
            public UserDTO createUser(CreateUserRequest request) {
                log.warn("[Fallback] createUser → null");
                return null;
            }

            @Override
            public UserDTO updateUserRole(String userId, String role) {
                log.warn("[Fallback] updateUserRole({}) → null", userId);
                return null;
            }

            @Override
            public UserDTO toggleUserActive(String userId) {
                log.warn("[Fallback] toggleUserActive({}) → null", userId);
                return null;
            }

            @Override
            public ResponseEntity<Void> validatePassword(Map<String, String> body) {
                log.warn("[Fallback] validatePassword → 503");
                return ResponseEntity.status(503).build();
            }
        };
    }
}
