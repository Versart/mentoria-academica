package com.versart.mentoria_academica.domain.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TokenService {

    private String secret = "12345678";

    public String gerarToken(Long userId, String role) {

        String stringUserId = userId.toString();
        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                .withIssuer("mentoria-academica")
                .withSubject(stringUserId)
                .withExpiresAt(pegarDataExpiracao())
                .withClaim("role", role)
                .sign(algorithm);
        }
        catch(JWTCreationException exception) {
            throw new JWTCreationException("", null);
        }
    }

    public DecodedJWT validarToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                .withIssuer("mentoria-academica")
                .build()
                .verify(token);
        }
        catch(JWTVerificationException ex) {
            throw new JWTVerificationException("Token inválido");
        }
    }

    public String getRole(String token) {
        return validarToken(token).getClaim("role").asString();
    }

    public String getSubject(String token) {
        return validarToken(token).getSubject();
    }

    public boolean isTokenValido(String token) {
        try {
            validarToken(token);
            return true;
        }
        catch(JWTVerificationException ex) {
            log.error("Token inválido");
            return false;
        }
    }

    private Instant pegarDataExpiracao() {
        return LocalDateTime.now().plusHours(1).toInstant(ZoneOffset.of("-03:00"));
    }
}
