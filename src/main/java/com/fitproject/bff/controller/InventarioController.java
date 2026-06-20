package com.fitproject.bff.controller;

import com.fitproject.bff.client.InventarioClient;
import com.fitproject.bff.dto.InsumoDTO;
import com.fitproject.bff.dto.TransaccionDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/inventory")
@RequiredArgsConstructor
@Slf4j
public class InventarioController {

    private final InventarioClient inventarioClient;

    @GetMapping("/insumos")
    public ResponseEntity<List<InsumoDTO>> getAll() {
        return ResponseEntity.ok(inventarioClient.getAll());
    }

    @GetMapping("/insumos/{id}")
    public ResponseEntity<InsumoDTO> getById(@PathVariable String id) {
        return ResponseEntity.ok(inventarioClient.getById(id));
    }

    @PostMapping("/insumos")
    public ResponseEntity<InsumoDTO> create(@RequestBody Map<String, Object> body) {
        log.info("POST /api/v1/inventory/insumos nombre={}", body.get("nombre"));
        return ResponseEntity.status(HttpStatus.CREATED).body(inventarioClient.create(body));
    }

    @PatchMapping("/insumos/{id}/stock")
    public ResponseEntity<InsumoDTO> addStock(@PathVariable String id,
                                               @RequestBody Map<String, Object> body) {
        log.info("PATCH /api/v1/inventory/insumos/{}/stock", id);
        return ResponseEntity.ok(inventarioClient.addStock(id, body));
    }

    @GetMapping("/insumos/{id}/transacciones")
    public ResponseEntity<List<TransaccionDTO>> getTransacciones(@PathVariable String id) {
        return ResponseEntity.ok(inventarioClient.getTransacciones(id));
    }
}
