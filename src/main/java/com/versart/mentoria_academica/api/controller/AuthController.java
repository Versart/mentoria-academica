package com.versart.mentoria_academica.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.versart.mentoria_academica.api.model.DadosSUAPResponse;
import com.versart.mentoria_academica.api.model.LoginRequest;
import com.versart.mentoria_academica.api.model.LoginResponse;
import com.versart.mentoria_academica.domain.service.SUAPService;
import com.versart.mentoria_academica.domain.service.TokenService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final SUAPService suapService;

    private final TokenService tokenService;

    @PostMapping
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        log.info("Requisição recebida para fazer login");
        DadosSUAPResponse dados = suapService.validarTokenEObterDados(loginRequest.tokenSuap());

        String role = dados.getTipoVinculo();

        Long userId = dados.getId();

        String nome = dados.getNomeUsual();

        return ResponseEntity.ok(new LoginResponse(tokenService.gerarToken(userId, role),userId,nome,role));
    }

    
}
