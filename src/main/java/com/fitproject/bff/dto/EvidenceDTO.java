package com.fitproject.bff.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EvidenceDTO {

    private String evidenceId;

    @NotBlank(message = "El ID del proyecto es requerido")
    private String projectId;

    @NotBlank(message = "El ID del paso es requerido")
    private String stepId;

    private String evidenceUrl;  // optional — absent when creating an ASSIGNED task

    private String description;

    @NotBlank(message = "El nombre es requerido")
    private String name;

    @NotBlank(message = "El autor es requerido")
    private String submittedBy;

    private String supervisorId;
    private String assignedWorkerId;
    private String assignedWorkerName;

    private String status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // Insumos consumed by the worker when submitting this evidence
    private List<InsumoUsadoDTO> insumosUsados;
}