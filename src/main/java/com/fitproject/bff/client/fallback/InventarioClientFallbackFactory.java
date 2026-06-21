package com.fitproject.bff.client.fallback;

import com.fitproject.bff.client.InventarioClient;
import com.fitproject.bff.dto.InsumoDTO;
import com.fitproject.bff.dto.TransaccionDTO;
import feign.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Fallback Factory para {@link InventarioClient}.
 *
 * <p>Proporciona degradación segura cuando MS-Inventario no está disponible.
 * Las consultas retornan colecciones vacías; las mutaciones retornan {@code null}.</p>
 *
 * @see InventarioClient
 */
@Slf4j
@Component
public class InventarioClientFallbackFactory implements FallbackFactory<InventarioClient> {

    /**
     * Crea una instancia fallback de {@link InventarioClient} que registra el fallo
     * y retorna valores seguros.
     *
     * @param cause excepción que activó el circuit breaker
     * @return implementación de degradación de {@link InventarioClient}
     */
    @Override
    public InventarioClient create(Throwable cause) {
        log.error("[CircuitBreaker] MS-Inventario no disponible: {}", cause.getMessage());
        return new InventarioClient() {

            @Override
            public List<InsumoDTO> getAll() {
                log.warn("[Fallback] getAll insumos → lista vacía");
                return Collections.emptyList();
            }

            @Override
            public InsumoDTO getById(String id) {
                log.warn("[Fallback] getById({}) → null", id);
                return null;
            }

            @Override
            public InsumoDTO create(Map<String, Object> body) {
                log.warn("[Fallback] create insumo → null");
                return null;
            }

            @Override
            public InsumoDTO addStock(String id, Map<String, Object> body) {
                log.warn("[Fallback] addStock({}) → null", id);
                return null;
            }

            @Override
            public List<TransaccionDTO> getTransacciones(String id) {
                log.warn("[Fallback] getTransacciones({}) → lista vacía", id);
                return Collections.emptyList();
            }
        };
    }
}
