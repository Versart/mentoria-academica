package com.versart.mentoria_academica.domain.exception;

public class NaoEncontradoException extends RuntimeException {
    public NaoEncontradoException(String mensagem){
        super(mensagem);
    }
}
