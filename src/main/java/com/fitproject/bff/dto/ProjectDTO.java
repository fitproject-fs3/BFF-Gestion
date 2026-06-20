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
 * Data Transfer Object para la entidad Project.
 * Utilizado para peticiones y respuestas de la API para evitar exponer entidades directamente.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectDTO {
    
    private String projectId;

    private String containerId;

    @NotBlank(message = "El nombre del modelo es requerido")
    private String modelName;

    private String description;

    private String imageUrl;

    private Double budget;

    private String supervisorId;

    private String supervisorName;

    @NotNull(message = "El progreso general es requerido")
    @Min(value = 0, message = "El progreso no puede ser menor a 0")
    @Max(value = 100, message = "El progreso no puede ser mayor a 100")
    private Integer overallProgress;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
    
    private List<StepDTO> constructionSteps;
    
    private List<EvidenceDTO> evidences;
}
