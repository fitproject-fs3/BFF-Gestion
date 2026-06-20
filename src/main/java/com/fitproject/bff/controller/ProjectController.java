package com.fitproject.bff.controller;

import com.fitproject.bff.client.GestionClient;
import com.fitproject.bff.dto.ProjectDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import java.util.List;

/**
 * Controlador REST para gestionar las operaciones relacionadas con proyectos
 * en el Backend For Frontend (BFF) de FitProject.
 * 
 * Este controlador actúa como intermediario entre las aplicaciones móviles
 * y el microservicio de gestión, proporcionando endpoints optimizados
 * para el frontend móvil.
 */
@RestController
@RequestMapping("/api/v1/mobile")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Proyectos Móvil", description = "API para gestión de proyectos desde aplicaciones móviles")
public class ProjectController {

    private final GestionClient gestionClient;

    /**
     * Obtiene todos los proyectos disponibles para la aplicación móvil.
     * 
     * Este endpoint delega la llamada al microservicio de gestión
     * y devuelve la lista completa de proyectos con sus pasos y evidencias.
     * 
     * @return ResponseEntity con la lista de proyectos
     */
    @GetMapping("/projects")
    @Operation(
        summary = "Obtener todos los proyectos",
        description = "Recupera la lista completa de proyectos desde el microservicio de gestión"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Proyectos obtenidos exitosamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<ProjectDTO>> getAllProjects() {
        log.info("Solicitando todos los proyectos desde el BFF móvil");
        try {
            List<ProjectDTO> projects = gestionClient.getAllProjects();
            log.info("Se recuperaron {} proyectos exitosamente", projects.size());
            return ResponseEntity.ok(projects);
        } catch (Exception e) {
            log.error("Error al obtener proyectos desde el microservicio de gestión", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/projects/{projectId}")
    @Operation(summary = "Obtener proyecto por ID")
    public ResponseEntity<ProjectDTO> getProjectById(@PathVariable String projectId) {
        log.info("Solicitando proyecto {} desde el BFF móvil", projectId);
        try {
            ProjectDTO project = gestionClient.getProjectById(projectId);
            return ResponseEntity.ok(project);
        } catch (Exception e) {
            log.error("Error al obtener proyecto {}", projectId, e);
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/projects")
    @Operation(summary = "Crear proyecto (solo ADMIN)")
    public ResponseEntity<ProjectDTO> createProject(@RequestBody Map<String, Object> body) {
        String modelName   = (String) body.get("modelName");
        String description = (String) body.get("description");
        String imageUrl    = (String) body.get("imageUrl");
        Double budget      = body.get("budget") != null ? Double.parseDouble(body.get("budget").toString()) : null;
        String supervisorId   = (String) body.get("supervisorId");
        String supervisorName = (String) body.get("supervisorName");
        log.info("POST /api/v1/mobile/projects modelName={} supervisorId={}", modelName, supervisorId);
        GestionClient.CreateProjectRequest req = new GestionClient.CreateProjectRequest(
                modelName, description, imageUrl, budget, supervisorId, supervisorName);
        return ResponseEntity.status(HttpStatus.CREATED).body(gestionClient.createProject(req));
    }
}
