package com.fitproject.bff.client;

import com.fitproject.bff.dto.InsumoDTO;
import com.fitproject.bff.dto.TransaccionDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(name = "ms-inventario", url = "${services.ms-inventario.url:http://localhost:8094/api/v1}")
public interface InventarioClient {

    @GetMapping("/insumos")
    List<InsumoDTO> getAll();

    @GetMapping("/insumos/{id}")
    InsumoDTO getById(@PathVariable("id") String id);

    @PostMapping("/insumos")
    InsumoDTO create(@RequestBody Map<String, Object> body);

    @PatchMapping("/insumos/{id}/stock")
    InsumoDTO addStock(@PathVariable("id") String id, @RequestBody Map<String, Object> body);

    @GetMapping("/insumos/{id}/transacciones")
    List<TransaccionDTO> getTransacciones(@PathVariable("id") String id);
}
