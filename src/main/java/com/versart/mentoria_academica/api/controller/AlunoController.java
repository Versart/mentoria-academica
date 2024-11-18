package com.versart.mentoria_academica.api.controller;

import com.versart.mentoria_academica.api.model.AlunoRequest;
import com.versart.mentoria_academica.api.model.AlunoResponse;
import com.versart.mentoria_academica.domain.service.AlunoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/alunos")
@RequiredArgsConstructor
public class AlunoController {

    private final AlunoService alunoService;

    @PostMapping
    public ResponseEntity<AlunoResponse> salvarAluno(@RequestBody @Valid AlunoRequest alunoRequest) {
        return new ResponseEntity<>(alunoService.salvarAluno(alunoRequest), HttpStatus.CREATED);
    }
    @GetMapping
    public String testando(){
        return "io";
    }
}
