package com.versart.mentoria_academica.api.controller;

import com.versart.mentoria_academica.api.model.LinhaDePesquisaRequest;
import com.versart.mentoria_academica.api.model.LinhaDePesquisaResponse;
import com.versart.mentoria_academica.domain.service.LinhaDePesquisaService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
@RequestMapping("/v1/linhas-de-pesquisa")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-key")
@Slf4j
public class LinhaDePesquisaController {

    private final LinhaDePesquisaService linhaDePesquisaService;

    @PostMapping
    public ResponseEntity<LinhaDePesquisaResponse> salvarEspecialidade(@RequestBody @Valid LinhaDePesquisaRequest linhaDePesquisaRequest) {
        log.info("Requisição recebida para criar uma linha de pesquisa");
        return new ResponseEntity<>(linhaDePesquisaService.salvarLinhaDePesquisa(linhaDePesquisaRequest), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<LinhaDePesquisaResponse>> listarEspecialidades(Pageable pageable) {
        log.info("Requisição recebida para buscar todas as linhas de pesquisa");
        return ResponseEntity.ok(linhaDePesquisaService.listarLinhasDePesquisa(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LinhaDePesquisaResponse> buscarEspecialidadePorId(@PathVariable UUID id) {
        log.info("Requisição recebida para buscar a linha de pesquisa com o id {}", id);
        return ResponseEntity.ok(linhaDePesquisaService.buscarLinhaDePesquisaPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LinhaDePesquisaResponse> alterarEspecialidade(@PathVariable UUID id, @RequestBody LinhaDePesquisaRequest linhaDePesquisaRequest) {
        log.info("Requisição recebida para alterar a linha de pesquisa com o id {}", id);
        return ResponseEntity.ok(linhaDePesquisaService.alterarEspecialidade(id,linhaDePesquisaRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarEspecialidade(@PathVariable UUID id) {
        log.info("Requisição recebida para remover a linha de pesquisa com o id {}", id);
        linhaDePesquisaService.deletarLinhaDePesquisa(id);
        return ResponseEntity.noContent().build();
    }
}
