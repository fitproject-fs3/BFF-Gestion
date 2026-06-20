package com.fitproject.bff.dto;

import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ProjectProgressDTO {
    private String projectId;
    private String modelName;
    private Integer overallProgress;
    private Integer stepsCompleted;
    private Integer totalSteps;
    private String createdAt;
}