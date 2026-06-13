package com.versart.mentoria_academica.api.controller;

import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.versart.mentoria_academica.api.model.DepartamentoResponse;
import com.versart.mentoria_academica.api.model.LinhaDePesquisaRequest;
import com.versart.mentoria_academica.api.model.LinhaDePesquisaResponse;
import com.versart.mentoria_academica.domain.model.LinhaDePesquisa;
import com.versart.mentoria_academica.domain.repository.LinhaDePesquisaRepository;
import com.versart.mentoria_academica.domain.service.TokenService;
import com.versart.mentoria_academica.util.LinhaDePesquisaCreator;
import com.versart.mentoria_academica.util.RestResponsePage;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = {
        "EMAIL_USERNAME=teste@dev.com",
        "EMAIL_PASSWORD=senhateste"
    }
)
@Testcontainers
@ActiveProfiles("dev")
class LinhaDePesquisaControllerIt {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private LinhaDePesquisaRepository linhaDePesquisaRepository;

    private static final String url  = "/v1/linhas-de-pesquisa";

    @BeforeEach
    void setup() {
        linhaDePesquisaRepository.save(LinhaDePesquisaCreator.criarLinhaDePesquisaComId());
        
        String token = tokenService.gerarToken(1l, "Professor");

        restTemplate.getRestTemplate().getInterceptors().add((request, body, execution) -> {
            request.getHeaders().setBearerAuth(token);
            return execution.execute(request, body);
        });
    }

    @AfterEach
    void fim() {
        linhaDePesquisaRepository.deleteAll();
    }

    @Test
    @DisplayName("salvarLinhaDePesquisa retorna linha de pesquisa quando bem sucedido")
    void salvarLinhaDePesquisa_RetornaLinhaDePesquisa_QuandoBemSucedido() {
        
        LinhaDePesquisaRequest linhaDePesquisaParaSerSalva = LinhaDePesquisaCreator.criarLinhaDePesquisaRequest("Inteligencia Artificial");
        HttpEntity<LinhaDePesquisaRequest> requestEntity = new HttpEntity<>(linhaDePesquisaParaSerSalva);

        ResponseEntity<LinhaDePesquisaResponse> response = restTemplate.exchange(url, 
                HttpMethod.POST, requestEntity,
                new ParameterizedTypeReference<LinhaDePesquisaResponse>() {
        });

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(response.getBody().getId()).isNotNull();
    }

    @Test
    @DisplayName("listarLinhasDePesquisa retorna page de linhas de pesquisa quando bem sucedido")
    void listarLinhasDePesquisa_RetornaPageDeLinhasDePesquisa_QuandoBemSucedido() {

        ResponseEntity<RestResponsePage<LinhaDePesquisaResponse>> response = restTemplate.exchange(url, 
                HttpMethod.GET, null,
                new ParameterizedTypeReference<RestResponsePage<LinhaDePesquisaResponse>>() {
        });

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody().getContent()).isNotEmpty();
    }

    @Test
    @DisplayName("buscarLinhaDePesquisaPorId retorna linha de pesquisa quando bem sucedido")
    void buscarLinhaDePesquisaPorId_RetornaLinhaDePesquisa_QuandoBemSucedido() {
        
        LinhaDePesquisa linhaDePesquisaSalva = linhaDePesquisaRepository.findAll().get(0);
        UUID idLinhaDePesquisa = linhaDePesquisaSalva.getId();
        
        ResponseEntity<DepartamentoResponse> response = restTemplate.exchange(url + "/" + idLinhaDePesquisa, 
                HttpMethod.GET, null,
                new ParameterizedTypeReference<DepartamentoResponse>() {
        });

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody().getId()).isEqualTo(idLinhaDePesquisa);
    }

    @Test
    @DisplayName("alterarLinhaDePesquisa retorna linha de pesquisa alterada quando bem sucedido")
    void alterarLinhaDePesquisa_RetornaLinhaDePesquisaAlterada_QuandoBemSucedido() {

        LinhaDePesquisa linhaDepesquisaSalva = linhaDePesquisaRepository.findAll().get(0);
        UUID idLinhaDePesquisa = linhaDepesquisaSalva.getId();
        LinhaDePesquisaRequest linhaDePesquisaAlterada = LinhaDePesquisaCreator.criarLinhaDePesquisaRequest();
        HttpEntity<LinhaDePesquisaRequest> requestEntity = new HttpEntity<>(linhaDePesquisaAlterada);

        ResponseEntity<DepartamentoResponse> response = restTemplate.exchange(url + "/" + idLinhaDePesquisa, 
                HttpMethod.PUT, requestEntity,
                new ParameterizedTypeReference<DepartamentoResponse>() {
        });

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody().getId()).isEqualTo(idLinhaDePesquisa);
        Assertions.assertThat(response.getBody().getNome()).isEqualTo(linhaDePesquisaAlterada.nome());
    }

    @Test
    @DisplayName("deletarLinhaDePesquisa remove linha de pesquisa quando bem sucedido")
    void deletarLinhaDePesquisa_RemoveLinhaDepesquisa_QuandoBemSucedido() {

        LinhaDePesquisa linhaDePesquisaSalva = linhaDePesquisaRepository.findAll().get(0);
        UUID idLinhaDePesquisa = linhaDePesquisaSalva.getId();

        ResponseEntity<Void> response = restTemplate.exchange(url + "/" + idLinhaDePesquisa, 
                HttpMethod.DELETE, null,
                new ParameterizedTypeReference<Void>() {
        });

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }


}
