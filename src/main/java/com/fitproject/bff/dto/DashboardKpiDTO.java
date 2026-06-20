package com.fitproject.bff.dto;

import lombok.*;
import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class DashboardKpiDTO {
    private Long totalProjects;
    private Long activeProjects;
    private Long completedProjects;
    private Integer averageProgress;
    private Long pendingEvidences;
    private Long approvedEvidences;
    private Long rejectedEvidences;
    private List<ProjectProgressDTO> projectsProgress;
}