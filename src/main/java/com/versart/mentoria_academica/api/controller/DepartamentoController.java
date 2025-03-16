package com.versart.mentoria_academica.api.controller;

import com.versart.mentoria_academica.api.model.DepartamentoRequest;
import com.versart.mentoria_academica.api.model.DepartamentoResponse;
import com.versart.mentoria_academica.domain.service.DepartamentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/departamentos")
@RequiredArgsConstructor
@Slf4j
public class DepartamentoController {

    private final DepartamentoService departamentoService;

    @PostMapping
    public ResponseEntity<DepartamentoResponse> salvarDepartamento(@Valid @RequestBody DepartamentoRequest departamentoRequest) {
        log.info("Requisição recebida para criar um departamento");
        return new ResponseEntity<>(departamentoService.salvarDepartamento(departamentoRequest), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<DepartamentoResponse>> listarDepartamentos(Pageable pageable) {
        log.info("Requisição recebida para buscar todos os departamentos");
        return ResponseEntity.ok(departamentoService.listarDepartamentos(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartamentoResponse> buscarDepartamentoPorId(@PathVariable UUID id) {
        log.info("Requisição recebida para buscar o departamento com o id {}", id);
        return ResponseEntity.ok(departamentoService.buscarDepartamentoPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DepartamentoResponse> alterarDepartamento(@PathVariable UUID id, @RequestBody DepartamentoRequest departamentoRequest) {
        log.info("Requisição recebida para alterar o departamento com o id {}", id);
        return ResponseEntity.ok(departamentoService.alterarDepartamento(id,departamentoRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarDepartamento(@PathVariable UUID id) {
        log.info("Requisição recebida para remover o departamento com o id {}", id);
        departamentoService.deletarDepartamento(id);
        return ResponseEntity.noContent().build();
    }
}
