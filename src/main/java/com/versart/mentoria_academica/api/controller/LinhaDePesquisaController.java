package com.versart.mentoria_academica.api.controller;

import com.versart.mentoria_academica.api.model.EspecialidadeRequest;
import com.versart.mentoria_academica.api.model.EspecialidadeResponse;
import com.versart.mentoria_academica.domain.service.EspecialidadeService;
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
@RequestMapping("/v1/especialidades")
@RequiredArgsConstructor
@Slf4j
public class EspecialidadeController {

    private final EspecialidadeService especialidadeService;

    @PostMapping
    public ResponseEntity<EspecialidadeResponse> salvarEspecialidade(@RequestBody @Valid EspecialidadeRequest especialidadeRequest) {
        log.info("Requisição recebida para criar uma especialiade");
        return new ResponseEntity<>(especialidadeService.salvarEspecialidade(especialidadeRequest), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<EspecialidadeResponse>> listarEspecialidades(Pageable pageable) {
        log.info("Requisição recebida para buscar todas as especialidades");
        return ResponseEntity.ok(especialidadeService.listarEspecialidades(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EspecialidadeResponse> buscarEspecialidadePorId(@PathVariable UUID id) {
        log.info("Requisição recebida para buscar a especialidade com o id {}", id);
        return ResponseEntity.ok(especialidadeService.buscarEspecialidadePorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EspecialidadeResponse> alterarEspecialidade(@PathVariable UUID id, @RequestBody EspecialidadeRequest especialidadeRequest) {
        log.info("Requisição recebida para alterar a especialidade com o id {}", id);
        return ResponseEntity.ok(especialidadeService.alterarEspecialidade(id,especialidadeRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarEspecialidade(@PathVariable UUID id) {
        log.info("Requisição recebida para remover a especialidade com o id {}", id);
        especialidadeService.deletarEspecialidade(id);
        return ResponseEntity.noContent().build();
    }
}
