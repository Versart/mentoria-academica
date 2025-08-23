package com.versart.mentoria_academica.api.controller;

import com.versart.mentoria_academica.api.model.MentorRequest;
import com.versart.mentoria_academica.api.model.MentorResponse;
import com.versart.mentoria_academica.domain.service.MentorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/mentores")
@RequiredArgsConstructor
@Slf4j
public class MentorController {

    private final MentorService mentorService;

    @PostMapping
    public ResponseEntity<MentorResponse> salvarMentor(@RequestBody @Valid MentorRequest mentorRequest) {
        log.info("Requisição recebida para criar um mentor");
        return new ResponseEntity<>(mentorService.salvarMentor(mentorRequest), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<MentorResponse>> listarMentores(@ParameterObject Pageable pageable) {
        log.info("Requisição recebida para buscar todos os mentores");
        return ResponseEntity.ok(mentorService.listarMentores(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MentorResponse> buscarMentorPorId(@PathVariable UUID id) {
        log.info("Requisição recebida para buscar o mentor com o id {}", id);
        return ResponseEntity.ok(mentorService.buscarMentorPorId(id));
    }

    @GetMapping("/find")
    public ResponseEntity<Page<MentorResponse>> buscarMentoresPorNome(@RequestParam(required = false) String nomeCompleto, @ParameterObject Pageable pageable) {
        log.info("Requisição recebida para buscar mentores que contenham {} no nome", nomeCompleto);
        return ResponseEntity.ok(mentorService.buscarMentoresPorNome(nomeCompleto,pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MentorResponse> alterarMentor(@PathVariable UUID id, @Valid @RequestBody MentorRequest mentorRequest) {
        log.info("Requisição recebida para alterar o mentor com o id {}", id);
        return ResponseEntity.ok(mentorService.alterarMentor(id,mentorRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarMentor(@PathVariable UUID id) {
        log.info("Requisição recebida para remover o mentor com o id {}", id);
        mentorService.deletarMentor(id);
        return ResponseEntity.noContent().build();
    }

}
