package com.fitproject.bff.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class TransaccionDTO {
    private String transaccionId;
    private String insumoId;
    private String insumoNombre;
    private String tipo;
    private Integer cantidad;
    private Integer stockResultante;
    private String referencia;
    private String realizadoPor;
    private LocalDateTime createdAt;
}
