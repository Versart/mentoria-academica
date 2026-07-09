package com.versart.mentoria_academica.api.controller;

import com.versart.mentoria_academica.api.model.LinhaDePesquisaRequest;
import com.versart.mentoria_academica.api.model.LinhaDePesquisaResponse;
import com.versart.mentoria_academica.domain.service.LinhaDePesquisaService;

import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "Cria uma linha de pesquisa", tags = "Linha de Pesquisa")
    public ResponseEntity<LinhaDePesquisaResponse> salvarLinhaDePesquisa(@RequestBody @Valid LinhaDePesquisaRequest linhaDePesquisaRequest) {
        log.info("Requisição recebida para criar uma linha de pesquisa");
        return new ResponseEntity<>(linhaDePesquisaService.salvarLinhaDePesquisa(linhaDePesquisaRequest), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Lista todas as linhas de pesquisa", tags = "Linha de Pesquisa")
    public ResponseEntity<Page<LinhaDePesquisaResponse>> listarLinhasDePesquisa(Pageable pageable) {
        log.info("Requisição recebida para buscar todas as linhas de pesquisa");
        return ResponseEntity.ok(linhaDePesquisaService.listarLinhasDePesquisa(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca uma linha de pesquisa pelo ID", tags = "Linha de Pesquisa")
    public ResponseEntity<LinhaDePesquisaResponse> buscarLinhaDePesquisaPorId(@PathVariable UUID id) {
        log.info("Requisição recebida para buscar a linha de pesquisa com o id {}", id);
        return ResponseEntity.ok(linhaDePesquisaService.buscarLinhaDePesquisaPorId(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Altera uma linha de pesquisa", tags = "Linha de Pesquisa")
    public ResponseEntity<LinhaDePesquisaResponse> alterarLinhaDePesquisa(@PathVariable UUID id, @RequestBody LinhaDePesquisaRequest linhaDePesquisaRequest) {
        log.info("Requisição recebida para alterar a linha de pesquisa com o id {}", id);
        return ResponseEntity.ok(linhaDePesquisaService.alterarLinhaDePesquisa(id,linhaDePesquisaRequest));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove uma linha de pesquisa", tags = "Linha de Pesquisa")
    public ResponseEntity<Void> deletarLinhaDepesquisa(@PathVariable UUID id) {
        log.info("Requisição recebida para remover a linha de pesquisa com o id {}", id);
        linhaDePesquisaService.deletarLinhaDePesquisa(id);
        return ResponseEntity.noContent().build();
    }
}
