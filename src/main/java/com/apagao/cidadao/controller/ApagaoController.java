package com.apagao.cidadao.controller;

import com.apagao.cidadao.model.ApagaoRequestDTO;
import com.apagao.cidadao.model.ApagaoResponseDTO;
import com.apagao.cidadao.service.ApagaoService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/apagao")
public class ApagaoController {

    @Autowired
    private ApagaoService service;

    @GetMapping
    @Operation(summary = "Listar todos os apagões")
    public List<ApagaoResponseDTO> listar() {
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar apagão por ID")
    public ResponseEntity<ApagaoResponseDTO> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Registrar um novo apagão")
    public ResponseEntity<ApagaoResponseDTO> criar(@RequestBody @Valid ApagaoRequestDTO dto) {
        return ResponseEntity.ok(service.salvar(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar apagão existente")
    public ResponseEntity<ApagaoResponseDTO> atualizar(@PathVariable Long id, @RequestBody @Valid ApagaoRequestDTO dto) {
        try {
            return ResponseEntity.ok(service.atualizar(id, dto));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar apagão")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
