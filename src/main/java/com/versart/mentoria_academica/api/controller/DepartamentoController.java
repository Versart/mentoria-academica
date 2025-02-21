package com.versart.mentoria_academica.api.controller;

import com.versart.mentoria_academica.api.model.DepartamentoRequest;
import com.versart.mentoria_academica.api.model.DepartamentoResponse;
import com.versart.mentoria_academica.domain.service.DepartamentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/departamentos")
@RequiredArgsConstructor
public class DepartamentoController {

    private final DepartamentoService departamentoService;

    @PostMapping
    public ResponseEntity<DepartamentoResponse> salvarDepartamento(@Valid @RequestBody DepartamentoRequest departamentoRequest) {
        return new ResponseEntity<>(departamentoService.salvarDepartamento(departamentoRequest), HttpStatus.CREATED);
    }
}
