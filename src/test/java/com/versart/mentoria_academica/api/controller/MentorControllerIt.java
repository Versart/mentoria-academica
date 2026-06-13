package com.versart.mentoria_academica.api.controller;


import java.util.Set;
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

import com.versart.mentoria_academica.api.model.MentorRequest;
import com.versart.mentoria_academica.api.model.MentorResponse;
import com.versart.mentoria_academica.domain.model.Departamento;
import com.versart.mentoria_academica.domain.model.LinhaDePesquisa;
import com.versart.mentoria_academica.domain.model.Mentor;
import com.versart.mentoria_academica.domain.repository.DepartamentoRepository;
import com.versart.mentoria_academica.domain.repository.LinhaDePesquisaRepository;
import com.versart.mentoria_academica.domain.repository.MentorRepository;
import com.versart.mentoria_academica.domain.service.TokenService;
import com.versart.mentoria_academica.util.DepartamentoCreator;
import com.versart.mentoria_academica.util.LinhaDePesquisaCreator;
import com.versart.mentoria_academica.util.MentorCreator;
import com.versart.mentoria_academica.util.RestResponsePage;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = {
        "EMAIL_USERNAME=teste@dev.com",
        "EMAIL_PASSWORD=senhateste"
    }
)
@Testcontainers
@ActiveProfiles("dev")
class MentorControllerIt {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");
    
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private MentorRepository mentorRepository;

    @Autowired
    private DepartamentoRepository departamentoRepository;

    @Autowired
    private LinhaDePesquisaRepository linhaDePesquisaRepository;

    private static final String url  = "/v1/mentores";

    @BeforeEach
    void setup() {
        Departamento departamentoSalvo = departamentoRepository.save(DepartamentoCreator.criarDepartamentoComId());
        LinhaDePesquisa linhaDePesquisaSalva = linhaDePesquisaRepository.save(LinhaDePesquisaCreator.criarLinhaDePesquisaComId());
        Mentor mentorParaSerSalvo = MentorCreator.criarMentorComId();

        mentorParaSerSalvo.setDepartamento(departamentoSalvo);
        mentorParaSerSalvo.setLinhasDePesquisa(Set.of(linhaDePesquisaSalva));
        mentorRepository.save(mentorParaSerSalvo);
        

        String token = tokenService.gerarToken(1l, "Professor");

        restTemplate.getRestTemplate().getInterceptors().add((request, body, execution) -> {
            request.getHeaders().setBearerAuth(token);
            return execution.execute(request, body);
        });
    }

    @AfterEach
    void fim() {
        mentorRepository.deleteAll();
        departamentoRepository.deleteAll();
        linhaDePesquisaRepository.deleteAll();
    }

    @Test
    @DisplayName("salvarMentor retorna mentor quando bem sucedido")
    void salvarMentor_RetornaMentor_QuandoBemSucedido() {
        MentorRequest mentorParaSerSalvo = MentorCreator.criarMentorRequest("212121","professor2@email.com");
        HttpEntity<MentorRequest> requestEntity = new HttpEntity<>(mentorParaSerSalvo);

        ResponseEntity<MentorResponse> response = restTemplate.exchange(url, 
                HttpMethod.POST, requestEntity,
                new ParameterizedTypeReference<MentorResponse>() {
        });

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(response.getBody().getId()).isNotNull();

    }

    @Test
    @DisplayName("listarMentores retorna page de mentores quando bem sucedido")
    void listarMentores_RetornaPageDeMentores_QuandoBemSucedido() throws Exception {
        
        ResponseEntity<RestResponsePage<MentorResponse>> response = restTemplate.exchange(url, 
                HttpMethod.GET, null,
                new ParameterizedTypeReference<RestResponsePage<MentorResponse>>() {
        });

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody().getContent()).isNotEmpty();
    }

    @Test
    @DisplayName("buscarMentorPorId retorna mentor quando bem sucedido")
    void buscarMentorPorId_RetornaMentor_QuandoBemsucedido() {
        Mentor mentorSalvo = mentorRepository.findAll().get(0);
        UUID idMentor = mentorSalvo.getId();
        
        ResponseEntity<MentorResponse> response = restTemplate.exchange(url + "/" + idMentor, 
                HttpMethod.GET, null,
                new ParameterizedTypeReference<MentorResponse>() {
        });

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody().getId()).isEqualTo(idMentor);
    }

    @Test
    @DisplayName("buscarMentoresPorNome retorna page de mentores quando bem sucedido")
    void buscarMentoresPorNome_RetornaPageDeMentores_QuandoBemSucedido() {
        String nomeEsperado = "Silva";

        ResponseEntity<RestResponsePage<MentorResponse>> response = restTemplate.exchange(url + "/find?nomeCOmpleto=" + nomeEsperado, 
                HttpMethod.GET, null,
                new ParameterizedTypeReference<RestResponsePage<MentorResponse>>() {
        });

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody().getContent().getFirst().getNomeCompleto()).contains(nomeEsperado);

    }

    @Test
    @DisplayName("alterarMentor retorna mentor alterado quando bem sucedido")
    void alterarMentor_RetornaMentorAlterado_QuandoBemSucedido() {
        Mentor mentorSalvo = mentorRepository.findAll().get(0);
        UUID idMentor = mentorSalvo.getId();
        MentorRequest mentorAlterado = MentorCreator.criarMentorRequest("212121","professor2@email.com");
        HttpEntity<MentorRequest> requestEntity = new HttpEntity<>(mentorAlterado);

        ResponseEntity<MentorResponse> response = restTemplate.exchange(url + "/" + idMentor, 
                HttpMethod.PUT, requestEntity,
                new ParameterizedTypeReference<MentorResponse>() {
        });

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody().getId()).isEqualTo(idMentor);
        Assertions.assertThat(response.getBody().getNomeCompleto()).isEqualTo(mentorAlterado.nomeCompleto());
    }

    @Test
    @DisplayName("deletarMentor remove mentor quando bem sucedido")
    void deletarMentor_RemoveMentor_QuandoBemSucedido() {
        Mentor mentorSalvo = mentorRepository.findAll().get(0);
        UUID idMentor = mentorSalvo.getId();

        ResponseEntity<Void> response = restTemplate.exchange(url + "/" + idMentor, 
                HttpMethod.DELETE, null,
                new ParameterizedTypeReference<Void>() {
        });

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

    }

}
