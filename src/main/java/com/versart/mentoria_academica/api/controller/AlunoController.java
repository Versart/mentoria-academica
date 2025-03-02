package com.versart.mentoria_academica.api.controller;

import com.versart.mentoria_academica.api.model.AlunoRequest;
import com.versart.mentoria_academica.api.model.AlunoResponse;
import com.versart.mentoria_academica.domain.service.AlunoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/alunos")
@RequiredArgsConstructor
public class AlunoController {

    private final AlunoService alunoService;

    @PostMapping
    public ResponseEntity<AlunoResponse> salvarAluno(@RequestBody @Valid AlunoRequest alunoRequest) {
        return new ResponseEntity<>(alunoService.salvarAluno(alunoRequest), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<AlunoResponse>> listarAlunos(Pageable pageable) {
        return ResponseEntity.ok(alunoService.listarAlunos(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlunoResponse> buscarAlunoPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(alunoService.buscarAlunoPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AlunoResponse> alterarAluno(@PathVariable UUID id, @RequestBody AlunoRequest alunoRequest) {
        return ResponseEntity.ok(alunoService.alterarAluno(id,alunoRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarAluno(@PathVariable UUID id) {
        alunoService.deletarAluno(id);
        return ResponseEntity.noContent().build();
    }

}
