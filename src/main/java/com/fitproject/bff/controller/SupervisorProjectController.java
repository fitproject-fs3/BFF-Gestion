package com.fitproject.bff.controller;

import com.fitproject.bff.client.GestionClient;
import com.fitproject.bff.dto.ProjectDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
@Slf4j
public class SupervisorProjectController {

    private final GestionClient gestionClient;

    @GetMapping
    public ResponseEntity<List<ProjectDTO>> getAllProjects() {
        log.info("GET /api/v1/projects");
        return ResponseEntity.ok(gestionClient.getAllProjects());
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectDTO> getProjectById(@PathVariable String projectId) {
        log.info("GET /api/v1/projects/{}", projectId);
        try {
            return ResponseEntity.ok(gestionClient.getProjectById(projectId));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{projectId}")
    public ResponseEntity<ProjectDTO> updateProject(
            @PathVariable String projectId,
            @RequestBody Map<String, Object> body) {
        String description = (String) body.get("description");
        String imageUrl = (String) body.get("imageUrl");
        Double budget = body.get("budget") != null ? Double.parseDouble(body.get("budget").toString()) : null;
        log.info("PATCH /api/v1/projects/{}", projectId);
        GestionClient.UpdateProjectRequest req = new GestionClient.UpdateProjectRequest(description, imageUrl, budget);
        return ResponseEntity.ok(gestionClient.updateProject(projectId, req));
    }
}
