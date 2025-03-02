package com.versart.mentoria_academica.api.controller;

import com.versart.mentoria_academica.api.model.MentorRequest;
import com.versart.mentoria_academica.api.model.MentorResponse;
import com.versart.mentoria_academica.domain.service.MentorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/mentores")
@RequiredArgsConstructor

public class MentorController {

    private final MentorService mentorService;

    @PostMapping
    public ResponseEntity<MentorResponse> salvarMentor(@RequestBody @Valid MentorRequest mentorRequest) {
        return new ResponseEntity<>(mentorService.salvarMentor(mentorRequest), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<MentorResponse>> listarMentores(Pageable pageable) {
        return ResponseEntity.ok(mentorService.listarMentores(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MentorResponse> buscarMentorPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(mentorService.buscarMentorPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MentorResponse> alterarAluno(@PathVariable UUID id, @RequestBody MentorRequest mentorRequest) {
        return ResponseEntity.ok(mentorService.alterarMentor(id,mentorRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarAluno(@PathVariable UUID id) {
        mentorService.deletarMentor(id);
        return ResponseEntity.noContent().build();
    }

}
