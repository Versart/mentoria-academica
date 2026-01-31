package com.versart.mentoria_academica.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.versart.mentoria_academica.api.model.EmailRequest;
import com.versart.mentoria_academica.api.model.EmailResponse;
import com.versart.mentoria_academica.domain.service.EmailService;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/v1/emails")
@RequiredArgsConstructor
@Slf4j
public class EmailController {

    private final EmailService emailService;

    
    @PostMapping
    public ResponseEntity<EmailResponse> enviarEmail (@Valid @RequestBody EmailRequest emailRequest) {
        log.info("Requisição recebida para enviar email a {}", emailRequest.emailProfessor());
        return ResponseEntity.ok(emailService.enviarEmail(emailRequest));
    }
}
