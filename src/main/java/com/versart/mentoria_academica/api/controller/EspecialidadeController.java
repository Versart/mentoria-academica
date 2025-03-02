package com.versart.mentoria_academica.api.controller;

import com.versart.mentoria_academica.api.model.EspecialidadeRequest;
import com.versart.mentoria_academica.api.model.EspecialidadeResponse;
import com.versart.mentoria_academica.domain.service.EspecialidadeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/especialidades")
@RequiredArgsConstructor
public class EspecialidadeController {

    private final EspecialidadeService especialidadeService;

    @PostMapping
    public ResponseEntity<EspecialidadeResponse> salvarEspecialidade(@RequestBody @Valid EspecialidadeRequest especialidadeRequest) {

        return new ResponseEntity<>(especialidadeService.salvarEspecialidade(especialidadeRequest), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<EspecialidadeResponse>> listarEspecialidades(Pageable pageable) {
        return ResponseEntity.ok(especialidadeService.listarEspecialidades(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EspecialidadeResponse> buscarEspecialidadePorId(@PathVariable UUID id) {
        return ResponseEntity.ok(especialidadeService.buscarEspecialidadePorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EspecialidadeResponse> alterarEspecialidade(@PathVariable UUID id, @RequestBody EspecialidadeRequest especialidadeRequest) {
        return ResponseEntity.ok(especialidadeService.alterarEspecialidade(id,especialidadeRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarEspecialidade(@PathVariable UUID id) {
        especialidadeService.deletarEspecialidade(id);
        return ResponseEntity.noContent().build();
    }
}
