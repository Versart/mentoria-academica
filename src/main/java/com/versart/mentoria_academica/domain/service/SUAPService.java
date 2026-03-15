package com.versart.mentoria_academica.domain.service;


import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.versart.mentoria_academica.api.model.DadosSUAPResponse;
import com.versart.mentoria_academica.api.model.TokenRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SUAPService {

    private static final String URL_VERIFICAR_TOKEN = "https://suap.ifma.edu.br/api/token/verify";

    private static final String URL_DADOS = "https://suap.ifma.edu.br/api/rh/meus-dados";

    private final RestTemplate restTemplate;

    //@Cacheable(value = "suapTokens", key = "#tokenSuap", unless = "#result == null")
    public DadosSUAPResponse validarTokenEObterDados(String tokenSUAP){
        log.info("Validando token SUAP");
        try {
            if(!verificarTokenSuap(tokenSUAP)) {
                return null;
            }

            DadosSUAPResponse dados = buscarDadosUsuario(tokenSUAP);

            return dados;
        }catch(Exception ex) {
            return null;
        }
    }

    private boolean verificarTokenSuap(String tokenSUAP) {
        TokenRequest tokenRequest = new TokenRequest(tokenSUAP);
        HttpEntity<TokenRequest> request = new HttpEntity<>(tokenRequest);
        ResponseEntity<String> respostaToken = restTemplate.exchange(URL_VERIFICAR_TOKEN, HttpMethod.POST,
        request,String.class );

        return respostaToken.getStatusCode().is2xxSuccessful();
    }

    private DadosSUAPResponse buscarDadosUsuario(String tokenSuap) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(tokenSuap);
        HttpEntity request = new HttpEntity<>(headers);
        ResponseEntity<DadosSUAPResponse> resposta = restTemplate.exchange(URL_DADOS, HttpMethod.GET,request,DadosSUAPResponse.class);

        if(resposta.getBody() == null) {
            throw new IllegalStateException("Sem resposta do SUAP");
        }

        return resposta.getBody();
    }
}
