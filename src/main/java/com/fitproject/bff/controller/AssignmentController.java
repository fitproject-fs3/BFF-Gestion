package com.fitproject.bff.controller;

import com.fitproject.bff.client.GestionClient;
import com.fitproject.bff.dto.TaskAssignmentDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/assignments")
@RequiredArgsConstructor
public class AssignmentController {

    private final GestionClient gestionClient;

    @PostMapping
    public ResponseEntity<TaskAssignmentDTO> create(@RequestBody Map<String, String> body) {
        return ResponseEntity.ok(gestionClient.createAssignment(body));
    }

    @GetMapping("/worker/{workerId}")
    public ResponseEntity<List<TaskAssignmentDTO>> getByWorker(@PathVariable String workerId) {
        return ResponseEntity.ok(gestionClient.getAssignmentsByWorker(workerId));
    }

    @GetMapping("/step/{stepId}")
    public ResponseEntity<List<TaskAssignmentDTO>> getByStep(@PathVariable String stepId) {
        return ResponseEntity.ok(gestionClient.getAssignmentsByStep(stepId));
    }

    @PatchMapping("/{assignmentId}/status")
    public ResponseEntity<TaskAssignmentDTO> updateStatus(
            @PathVariable String assignmentId,
            @RequestParam String status) {
        return ResponseEntity.ok(gestionClient.updateAssignmentStatus(assignmentId, status));
    }

    @DeleteMapping("/{assignmentId}")
    public ResponseEntity<Void> delete(@PathVariable String assignmentId) {
        gestionClient.deleteAssignment(assignmentId);
        return ResponseEntity.noContent().build();
    }
}
