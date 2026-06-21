package com.fitproject.bff.client.fallback;

import com.fitproject.bff.client.GestionClient;
import com.fitproject.bff.dto.*;
import feign.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Fallback Factory para {@link GestionClient}.
 *
 * <p>Proporciona respuestas de degradación segura cuando el Circuit Breaker abre
 * el circuito hacia MS-Gestion. Las lecturas devuelven colecciones vacías;
 * las escrituras devuelven {@code null} para que el controlador detecte
 * la indisponibilidad y responda con 503.</p>
 *
 * @see GestionClient
 */
@Slf4j
@Component
public class GestionClientFallbackFactory implements FallbackFactory<GestionClient> {

    /**
     * Crea una instancia fallback de {@link GestionClient} que registra la causa
     * de fallo y retorna valores seguros en cada método.
     *
     * @param cause excepción que activó el circuit breaker
     * @return implementación de degradación de {@link GestionClient}
     */
    @Override
    public GestionClient create(Throwable cause) {
        log.error("[CircuitBreaker] MS-Gestion no disponible: {}", cause.getMessage());
        return new GestionClient() {

            @Override
            public List<ProjectDTO> getAllProjects() {
                log.warn("[Fallback] getAllProjects → lista vacía");
                return Collections.emptyList();
            }

            @Override
            public ProjectDTO getProjectById(String projectId) {
                log.warn("[Fallback] getProjectById({}) → null", projectId);
                return null;
            }

            @Override
            public ProjectDTO createProject(CreateProjectRequest request) {
                log.warn("[Fallback] createProject → null (servicio no disponible)");
                return null;
            }

            @Override
            public ProjectDTO updateProject(String projectId, UpdateProjectRequest request) {
                log.warn("[Fallback] updateProject({}) → null", projectId);
                return null;
            }

            @Override
            public List<EvidenceDTO> getEvidenceByStep(String stepId) {
                log.warn("[Fallback] getEvidenceByStep({}) → lista vacía", stepId);
                return Collections.emptyList();
            }

            @Override
            public List<EvidenceDTO> getPendingEvidences() {
                log.warn("[Fallback] getPendingEvidences → lista vacía");
                return Collections.emptyList();
            }

            @Override
            public List<EvidenceDTO> getEvidenceByWorker(String workerId) {
                log.warn("[Fallback] getEvidenceByWorker({}) → lista vacía", workerId);
                return Collections.emptyList();
            }

            @Override
            public EvidenceDTO submitEvidence(EvidenceDTO request) {
                log.warn("[Fallback] submitEvidence → null (servicio no disponible)");
                return null;
            }

            @Override
            public EvidenceDTO workerSubmitEvidence(String evidenceId, EvidenceDTO request) {
                log.warn("[Fallback] workerSubmitEvidence({}) → null", evidenceId);
                return null;
            }

            @Override
            public void deleteEvidence(String evidenceId) {
                log.warn("[Fallback] deleteEvidence({}) → sin efecto", evidenceId);
            }

            @Override
            public EvidenceDTO approveEvidence(String evidenceId, String supervisorId) {
                log.warn("[Fallback] approveEvidence({}) → null", evidenceId);
                return null;
            }

            @Override
            public EvidenceDTO rejectEvidence(String evidenceId, String supervisorId) {
                log.warn("[Fallback] rejectEvidence({}) → null", evidenceId);
                return null;
            }

            @Override
            public TaskAssignmentDTO createAssignment(Map<String, String> body) {
                log.warn("[Fallback] createAssignment → null");
                return null;
            }

            @Override
            public List<TaskAssignmentDTO> getAssignmentsByWorker(String workerId) {
                log.warn("[Fallback] getAssignmentsByWorker({}) → lista vacía", workerId);
                return Collections.emptyList();
            }

            @Override
            public List<TaskAssignmentDTO> getAssignmentsByStep(String stepId) {
                log.warn("[Fallback] getAssignmentsByStep({}) → lista vacía", stepId);
                return Collections.emptyList();
            }

            @Override
            public TaskAssignmentDTO updateAssignmentStatus(String assignmentId, String status) {
                log.warn("[Fallback] updateAssignmentStatus({}) → null", assignmentId);
                return null;
            }

            @Override
            public void deleteAssignment(String assignmentId) {
                log.warn("[Fallback] deleteAssignment({}) → sin efecto", assignmentId);
            }

            @Override
            public StepDTO createStep(CreateStepRequest request) {
                log.warn("[Fallback] createStep → null");
                return null;
            }

            @Override
            public StepDTO renameStep(String stepId, Map<String, String> body) {
                log.warn("[Fallback] renameStep({}) → null", stepId);
                return null;
            }
        };
    }
}
