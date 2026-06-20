package com.fitproject.bff.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class InsumoDTO {
    private String insumoId;
    private String nombre;
    private String descripcion;
    private Integer cantidadDisponible;
    private String unidadMedida;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
