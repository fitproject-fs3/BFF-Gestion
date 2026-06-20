package com.fitproject.bff.controller;

import com.fitproject.bff.client.GestionClient;
import com.fitproject.bff.dto.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
@Tag(name = "Dashboard", description = "KPIs para inversionistas y administradores")
public class DashboardController {

    private final GestionClient gestionClient;

    @GetMapping("/kpis")
    public ResponseEntity<DashboardKpiDTO> getKpis() {
        log.info("Dashboard KPIs requested");
        List<ProjectDTO> projects = gestionClient.getAllProjects();

        long active    = projects.stream().filter(p -> p.getOverallProgress() < 100).count();
        long completed = projects.stream().filter(p -> p.getOverallProgress() >= 100).count();

        double avgProgress = projects.stream()
            .mapToInt(ProjectDTO::getOverallProgress)
            .average()
            .orElse(0);

        long pending = projects.stream()
            .flatMap(p -> p.getEvidences().stream())
            .filter(e -> "PENDING".equals(e.getStatus()))
            .count();

        long approved = projects.stream()
            .flatMap(p -> p.getEvidences().stream())
            .filter(e -> "APPROVED".equals(e.getStatus()))
            .count();

        long rejected = projects.stream()
            .flatMap(p -> p.getEvidences().stream())
            .filter(e -> "REJECTED".equals(e.getStatus()))
            .count();

        List<ProjectProgressDTO> progress = projects.stream()
            .map(p -> ProjectProgressDTO.builder()
                .projectId(p.getProjectId())
                .modelName(p.getModelName())
                .overallProgress(p.getOverallProgress())
                .stepsCompleted((int) p.getConstructionSteps().stream()
                    .filter(s -> Boolean.TRUE.equals(s.getStepStatus()))
                    .count())
                .totalSteps(p.getConstructionSteps() != null ? p.getConstructionSteps().size() : 0)
                .createdAt(p.getCreatedAt() != null ? p.getCreatedAt().toString() : null)
                .build())
            .collect(Collectors.toList());

        return ResponseEntity.ok(DashboardKpiDTO.builder()
            .totalProjects((long) projects.size())
            .activeProjects(active)
            .completedProjects(completed)
            .averageProgress((int) Math.round(avgProgress))
            .pendingEvidences(pending)
            .approvedEvidences(approved)
            .rejectedEvidences(rejected)
            .projectsProgress(progress)
            .build());
    }
}