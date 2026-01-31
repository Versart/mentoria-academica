package com.versart.mentoria_academica.domain.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.versart.mentoria_academica.api.model.EmailRequest;
import com.versart.mentoria_academica.api.model.EmailResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    
    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String remetente; 

    private final String assunto = "Orientador TCC/Artigo";

    public EmailResponse enviarEmail(EmailRequest emailRequest) {
        log.info("Enviando o email a {}", emailRequest.emailProfessor());
        try{
            SimpleMailMessage email = new SimpleMailMessage();
            email.setFrom(remetente);
            email.setTo(emailRequest.emailProfessor());
            email.setSubject(assunto);
            email.setText(emailRequest.resumo());
            javaMailSender.send(email);
            return EmailResponse.builder().mensagem("Email enviado com sucesso!").build();
        }catch(MailException ex) {
            throw  ex; 
        }

    }
}
