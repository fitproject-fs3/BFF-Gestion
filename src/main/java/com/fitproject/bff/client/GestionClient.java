package com.fitproject.bff.client;

import com.fitproject.bff.dto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "ms-gestion", url = "${services.ms-gestion.url:http://localhost:8080/api/v1}")
public interface GestionClient {

    // ── Proyectos ──────────────────────────────────────────────────
    @GetMapping("/projects")
    List<ProjectDTO> getAllProjects();

    @GetMapping("/projects/{projectId}")
    ProjectDTO getProjectById(@PathVariable("projectId") String projectId);

    @PostMapping("/projects")
    ProjectDTO createProject(@RequestBody CreateProjectRequest request);

    @PatchMapping("/projects/{projectId}")
    ProjectDTO updateProject(@PathVariable("projectId") String projectId,
                             @RequestBody UpdateProjectRequest request);

    // ── Evidencias ─────────────────────────────────────────────────
    @GetMapping("/evidences/step/{stepId}")
    List<EvidenceDTO> getEvidenceByStep(@PathVariable("stepId") String stepId);

    @GetMapping("/evidences/pending")
    List<EvidenceDTO> getPendingEvidences();

    @GetMapping("/evidences/worker/{workerId}")
    List<EvidenceDTO> getEvidenceByWorker(@PathVariable("workerId") String workerId);

    @PostMapping("/evidences/submit")
    EvidenceDTO submitEvidence(@RequestBody EvidenceDTO request);

    @PostMapping("/evidences/{evidenceId}/worker-submit")
    EvidenceDTO workerSubmitEvidence(@PathVariable("evidenceId") String evidenceId,
                                     @RequestBody EvidenceDTO request);

    @DeleteMapping("/evidences/{evidenceId}")
    void deleteEvidence(@PathVariable("evidenceId") String evidenceId);

    @PostMapping("/evidences/{evidenceId}/approve")
    EvidenceDTO approveEvidence(@PathVariable("evidenceId") String evidenceId,
                                @RequestParam("supervisorId") String supervisorId);

    @PostMapping("/evidences/{evidenceId}/reject")
    EvidenceDTO rejectEvidence(@PathVariable("evidenceId") String evidenceId,
                               @RequestParam("supervisorId") String supervisorId);

    // ── Asignaciones ───────────────────────────────────────────────
    @PostMapping("/assignments")
    TaskAssignmentDTO createAssignment(@RequestBody java.util.Map<String, String> body);

    @GetMapping("/assignments/worker/{workerId}")
    java.util.List<TaskAssignmentDTO> getAssignmentsByWorker(@PathVariable("workerId") String workerId);

    @GetMapping("/assignments/step/{stepId}")
    java.util.List<TaskAssignmentDTO> getAssignmentsByStep(@PathVariable("stepId") String stepId);

    @PatchMapping("/assignments/{assignmentId}/status")
    TaskAssignmentDTO updateAssignmentStatus(@PathVariable("assignmentId") String assignmentId,
                                             @RequestParam("status") String status);

    @DeleteMapping("/assignments/{assignmentId}")
    void deleteAssignment(@PathVariable("assignmentId") String assignmentId);

    // ── Pasos ──────────────────────────────────────────────────────
    @PostMapping("/steps")
    StepDTO createStep(@RequestBody CreateStepRequest request);

    @PostMapping("/steps/{stepId}/name")
    StepDTO renameStep(@PathVariable("stepId") String stepId,
                       @RequestBody java.util.Map<String, String> body);

    // ── Inner request DTOs ─────────────────────────────────────────
    class CreateProjectRequest {
        private String modelName;
        private String description;
        private String imageUrl;
        private Double budget;
        private String supervisorId;
        private String supervisorName;

        public CreateProjectRequest() {}
        public CreateProjectRequest(String modelName, String description, String imageUrl, Double budget, String supervisorId, String supervisorName) {
            this.modelName = modelName;
            this.description = description;
            this.imageUrl = imageUrl;
            this.budget = budget;
            this.supervisorId = supervisorId;
            this.supervisorName = supervisorName;
        }
        public String getModelName() { return modelName; }
        public void setModelName(String m) { this.modelName = m; }
        public String getDescription() { return description; }
        public void setDescription(String d) { this.description = d; }
        public String getImageUrl() { return imageUrl; }
        public void setImageUrl(String u) { this.imageUrl = u; }
        public Double getBudget() { return budget; }
        public void setBudget(Double b) { this.budget = b; }
        public String getSupervisorId() { return supervisorId; }
        public void setSupervisorId(String s) { this.supervisorId = s; }
        public String getSupervisorName() { return supervisorName; }
        public void setSupervisorName(String s) { this.supervisorName = s; }
    }

    class UpdateProjectRequest {
        private String description;
        private String imageUrl;
        private Double budget;

        public UpdateProjectRequest() {}
        public UpdateProjectRequest(String description, String imageUrl, Double budget) {
            this.description = description;
            this.imageUrl = imageUrl;
            this.budget = budget;
        }
        public String getDescription() { return description; }
        public void setDescription(String d) { this.description = d; }
        public String getImageUrl() { return imageUrl; }
        public void setImageUrl(String u) { this.imageUrl = u; }
        public Double getBudget() { return budget; }
        public void setBudget(Double b) { this.budget = b; }
    }

}