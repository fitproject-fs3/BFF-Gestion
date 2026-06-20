package com.fitproject.bff.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Data Transfer Object para la entidad ConstructionStep.
 * Utilizado para peticiones y respuestas de la API para evitar exponer entidades directamente.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StepDTO {
    
    private String stepId;
    
    @NotBlank(message = "El ID del proyecto es requerido")
    private String projectId;
    
    @NotBlank(message = "El nombre del paso es requerido")
    private String stepName;
    
    @NotNull(message = "El estado del paso es requerido")
    private Boolean stepStatus;
    
    @NotNull(message = "El valor del progreso es requerido")
    @Min(value = 0, message = "El progreso no puede ser menor a 0")
    @Max(value = 100, message = "El progreso no puede ser mayor a 100")
    private Integer progressValue;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    private List<EvidenceDTO> evidences;
}
