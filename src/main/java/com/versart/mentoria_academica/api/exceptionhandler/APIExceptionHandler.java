package com.versart.mentoria_academica.api.exceptionhandler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class APIExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders cabecalhoHttp, HttpStatusCode status, WebRequest request) {
        List<Campo> campos = new ArrayList<>();

        for(FieldError fieldError: ex.getFieldErrors()) {
            String nome = fieldError.getField();
            String mensagem = fieldError.getDefaultMessage();
            campos.add(new Campo(nome,mensagem));
        }

        Problema problema = Problema.builder()
                .statusCode(400)
                .messagem("Campos inválidos")
                .dataHora(OffsetDateTime.now())
                .campos(campos)
                .build();
        return handleExceptionInternal(ex,problema,cabecalhoHttp,status,request);
    }
}
