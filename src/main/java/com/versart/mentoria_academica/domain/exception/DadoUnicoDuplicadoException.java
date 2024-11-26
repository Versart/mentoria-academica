package com.versart.mentoria_academica.domain.exception;

public class DadoUnicoDuplicadoException extends RuntimeException{
    public DadoUnicoDuplicadoException(String mensagem){
        super(mensagem);
    }
}
