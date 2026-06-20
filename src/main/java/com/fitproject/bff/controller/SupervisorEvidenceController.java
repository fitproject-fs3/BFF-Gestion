package com.fitproject.bff.controller;

import com.fitproject.bff.client.GestionClient;
import com.fitproject.bff.client.UsersClient;
import com.fitproject.bff.dto.EvidenceDTO;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/evidence")
@RequiredArgsConstructor
@Slf4j
public class SupervisorEvidenceController {

    private final GestionClient gestionClient;
    private final UsersClient usersClient;

    @GetMapping("/step/{stepId}")
    public ResponseEntity<List<EvidenceDTO>> getByStep(@PathVariable String stepId) {
        log.info("GET /api/v1/evidence/step/{}", stepId);
        return ResponseEntity.ok(gestionClient.getEvidenceByStep(stepId));
    }

    @GetMapping("/pending")
    public ResponseEntity<List<EvidenceDTO>> getPending() {
        log.info("GET /api/v1/evidence/pending");
        return ResponseEntity.ok(gestionClient.getPendingEvidences());
    }

    @GetMapping("/worker/{workerId}")
    public ResponseEntity<List<EvidenceDTO>> getByWorker(@PathVariable String workerId) {
        log.info("GET /api/v1/evidence/worker/{}", workerId);
        return ResponseEntity.ok(gestionClient.getEvidenceByWorker(workerId));
    }

    @PostMapping
    public ResponseEntity<EvidenceDTO> submit(@RequestBody EvidenceDTO request) {
        log.info("POST /api/v1/evidence stepId={}", request.getStepId());
        return ResponseEntity.ok(gestionClient.submitEvidence(request));
    }

    @PostMapping("/{evidenceId}/approve")
    public ResponseEntity<EvidenceDTO> approve(
            @PathVariable String evidenceId,
            @RequestParam(required = false, defaultValue = "") String supervisorId) {
        log.info("POST /api/v1/evidence/{}/approve supervisorId={}", evidenceId, supervisorId);
        return ResponseEntity.ok(gestionClient.approveEvidence(evidenceId, supervisorId));
    }

    @PostMapping("/{evidenceId}/reject")
    public ResponseEntity<EvidenceDTO> reject(
            @PathVariable String evidenceId,
            @RequestParam(required = false, defaultValue = "") String supervisorId) {
        log.info("POST /api/v1/evidence/{}/reject supervisorId={}", evidenceId, supervisorId);
        return ResponseEntity.ok(gestionClient.rejectEvidence(evidenceId, supervisorId));
    }

    @PostMapping("/{evidenceId}/worker-submit")
    public ResponseEntity<EvidenceDTO> workerSubmit(
            @PathVariable String evidenceId,
            @RequestBody EvidenceDTO req) {
        log.info("POST /api/v1/evidence/{}/worker-submit", evidenceId);
        return ResponseEntity.ok(gestionClient.workerSubmitEvidence(evidenceId, req));
    }

    /**
     * Secure delete: validates the supervisor's password against MS-Users before deleting.
     * Body: { "email": "...", "password": "..." }
     */
    @PostMapping("/{evidenceId}/delete")
    public ResponseEntity<Void> deleteEvidence(
            @PathVariable String evidenceId,
            @RequestBody Map<String, String> body) {
        log.info("POST /api/v1/evidence/{}/delete", evidenceId);
        String email    = body.get("email");
        String password = body.get("password");
        if (email == null || email.isBlank() || password == null || password.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        try {
            usersClient.validatePassword(Map.of("email", email, "password", password));
        } catch (FeignException e) {
            log.warn("Password validation failed for {}: {}", email, e.status());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        gestionClient.deleteEvidence(evidenceId);
        return ResponseEntity.noContent().build();
    }
}
