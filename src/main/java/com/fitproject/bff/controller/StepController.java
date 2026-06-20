package com.fitproject.bff.controller;

import com.fitproject.bff.client.GestionClient;
import com.fitproject.bff.dto.CreateStepRequest;
import com.fitproject.bff.dto.StepDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/steps")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class StepController {

    private final GestionClient gestionClient;

    @PostMapping
    public ResponseEntity<StepDTO> create(@RequestBody CreateStepRequest req) {
        log.info("POST /api/v1/steps projectId={}", req.getProjectId());
        return ResponseEntity.status(HttpStatus.CREATED).body(gestionClient.createStep(req));
    }

    @PostMapping("/{stepId}/name")
    public ResponseEntity<StepDTO> rename(
            @PathVariable String stepId,
            @RequestBody Map<String, String> body) {
        log.info("POST /api/v1/steps/{}/name", stepId);
        return ResponseEntity.ok(gestionClient.renameStep(stepId, body));
    }
}
