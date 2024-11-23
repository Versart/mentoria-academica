package com.versart.mentoria_academica.api.exceptionhandler;

import com.versart.mentoria_academica.domain.exception.NaoEncontradoException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
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
    @ExceptionHandler(NaoEncontradoException.class)
    public ResponseEntity<Object> handleNaoEncontradoException(NaoEncontradoException ex, WebRequest request){
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        Problema problema = Problema.builder()
                .statusCode(httpStatus.value())
                .dataHora(OffsetDateTime.now())
                .messagem(ex.getMessage())
                .build();

        return handleExceptionInternal(ex,problema,new HttpHeaders(),httpStatus,request);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request){
        Problema problema = Problema.builder()
                .statusCode(status.value())
                .messagem("Id não encontrado")
                .dataHora(OffsetDateTime.now())
                .build();

        return handleExceptionInternal(ex,problema,new HttpHeaders(),status,request);

    }
}
