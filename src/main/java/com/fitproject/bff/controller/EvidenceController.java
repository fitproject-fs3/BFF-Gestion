package com.fitproject.bff.controller;

import com.fitproject.bff.client.GestionClient;
import com.fitproject.bff.dto.EvidenceDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/mobile/evidences")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
@Tag(name = "Evidencias Móvil", description = "API para gestión de evidencias")
public class EvidenceController {

    private final GestionClient gestionClient;

    @PostMapping("/submit")
    public ResponseEntity<EvidenceDTO> submitEvidence(@RequestBody EvidenceDTO request) {
        log.info("BFF: Submitting evidence for step: {}", request.getStepId());
        return ResponseEntity.ok(gestionClient.submitEvidence(request));
    }

    @PostMapping("/{evidenceId}/approve")
    public ResponseEntity<EvidenceDTO> approveEvidence(
            @PathVariable String evidenceId,
            @RequestParam String supervisorId) {
        log.info("BFF: Approving evidence: {}", evidenceId);
        return ResponseEntity.ok(gestionClient.approveEvidence(evidenceId, supervisorId));
    }

    @PostMapping("/{evidenceId}/reject")
    public ResponseEntity<EvidenceDTO> rejectEvidence(
            @PathVariable String evidenceId,
            @RequestParam String supervisorId) {
        log.info("BFF: Rejecting evidence: {}", evidenceId);
        return ResponseEntity.ok(gestionClient.rejectEvidence(evidenceId, supervisorId));
    }
}