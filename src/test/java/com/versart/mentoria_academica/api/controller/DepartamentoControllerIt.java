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

import com.versart.mentoria_academica.api.model.DepartamentoRequest;
import com.versart.mentoria_academica.api.model.DepartamentoResponse;
import com.versart.mentoria_academica.domain.model.Departamento;
import com.versart.mentoria_academica.domain.repository.DepartamentoRepository;
import com.versart.mentoria_academica.domain.service.TokenService;
import com.versart.mentoria_academica.util.DepartamentoCreator;
import com.versart.mentoria_academica.util.RestResponsePage;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = {
        "EMAIL_USERNAME=teste@dev.com",
        "EMAIL_PASSWORD=senhateste"
    }
)
@Testcontainers
@ActiveProfiles("dev")
class DepartamentoControllerIt {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");
    
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private DepartamentoRepository departamentoRepository;

    private static final String url  = "/v1/departamentos";

    @BeforeEach
    void setup() {
        departamentoRepository.save(DepartamentoCreator.criarDepartamentoSemId());
        
        String token = tokenService.gerarToken(1l, "Professor");

        restTemplate.getRestTemplate().getInterceptors().add((request, body, execution) -> {
            request.getHeaders().setBearerAuth(token);
            return execution.execute(request, body);
        });
    }

    @AfterEach
    void fim() {
        departamentoRepository.deleteAll();
    }


    @Test
    @DisplayName("salvarDepartamento retorna departamento quando bem sucedido")
    void salvarDepartamento_RetornaMentor_QuandoBemSucedido() {
        DepartamentoRequest departamentoParaSerSalvo = DepartamentoCreator.criarDepartamentoRequest();
        HttpEntity<DepartamentoRequest> requestEntity = new HttpEntity<>(departamentoParaSerSalvo);

        ResponseEntity<DepartamentoResponse> response = restTemplate.exchange(url, 
                HttpMethod.POST, requestEntity,
                new ParameterizedTypeReference<DepartamentoResponse>() {
        });

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(response.getBody().getId()).isNotNull();
    } 

    @Test
    @DisplayName("listarDepartamentos retorna page de de departamentos quando bem sucedido")
    void listarDepartamentos_RetornaPageDeDepartamentos_QuandoBemSucedido() {

        ResponseEntity<RestResponsePage<DepartamentoResponse>> response = restTemplate.exchange(url, 
                HttpMethod.GET, null,
                new ParameterizedTypeReference<RestResponsePage<DepartamentoResponse>>() {
        });

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody().getContent()).isNotEmpty();
    }

    @Test
    @DisplayName("buscarDepartamentoPorId retorna departamento quando bem sucedido")
    void buscarDepartamentoPorId_RetornaDepartamento_QuandoBemSucedido() {

        Departamento departamentoSalvo = departamentoRepository.findAll().get(0);
        UUID idDepartamento = departamentoSalvo.getId();
        
        ResponseEntity<DepartamentoResponse> response = restTemplate.exchange(url + "/" + idDepartamento, 
                HttpMethod.GET, null,
                new ParameterizedTypeReference<DepartamentoResponse>() {
        });

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody().getId()).isEqualTo(idDepartamento);
    }

    @Test
    @DisplayName("alterarDepartamento retorna departamento alterado quando bem sucedido")
    void alterarDepartamento_RetornaDepartamentoAlterado_QuandoBemSucedido() {

        Departamento departamentoSalvo = departamentoRepository.findAll().get(0);
        UUID idDepartamento = departamentoSalvo.getId();
        DepartamentoRequest departamentoAlterado = DepartamentoCreator.criarDepartamentoRequest();
        HttpEntity<DepartamentoRequest> requestEntity = new HttpEntity<>(departamentoAlterado);

        ResponseEntity<DepartamentoResponse> response = restTemplate.exchange(url + "/" + idDepartamento, 
                HttpMethod.PUT, requestEntity,
                new ParameterizedTypeReference<DepartamentoResponse>() {
        });

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody().getId()).isEqualTo(idDepartamento);
        Assertions.assertThat(response.getBody().getNome()).isEqualTo(departamentoAlterado.nome());
    }

    @Test
    @DisplayName("deletarDepartamento remove departamento quando bem sucedido")
    void deletarDepartamento_RemoveDepartamento_QuandoBemSucedido() {

        Departamento departamentoSalvo = departamentoRepository.findAll().get(0);
        UUID idDepartamento = departamentoSalvo.getId();

        ResponseEntity<Void> response = restTemplate.exchange(url + "/" + idDepartamento, 
                HttpMethod.DELETE, null,
                new ParameterizedTypeReference<Void>() {
        });

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}
