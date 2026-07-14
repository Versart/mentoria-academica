package com.versart.mentoria_academica.api.controller;

import java.util.List;
import java.util.UUID;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.versart.mentoria_academica.api.model.MentorRequest;
import com.versart.mentoria_academica.api.model.MentorResponse;
import com.versart.mentoria_academica.config.SecurityTestConfig;
import com.versart.mentoria_academica.domain.repository.DepartamentoRepository;
import com.versart.mentoria_academica.domain.repository.LinhaDePesquisaRepository;
import com.versart.mentoria_academica.domain.repository.MentorRepository;
import com.versart.mentoria_academica.domain.service.MentorService;
import com.versart.mentoria_academica.domain.service.TokenService;
import com.versart.mentoria_academica.util.MentorCreator;


@WebMvcTest(controllers = MentorController.class)
@Import({SecurityTestConfig.class})
class MentorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MentorRepository mentorRepository;

    @MockBean
    private DepartamentoRepository departamentoRepository;

    @MockBean
    private LinhaDePesquisaRepository linhaDePesquisaRepository;

    @MockBean
    private TokenService tokenService;

    /*@MockBean
    private CacheManager cacheManager;*/

    @MockBean
    private MentorService mentorService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("salvarMentor retorna mentor quando bem sucedido")
    void salvarMentor_RetornaMentor_QuandoBemSucedido() throws Exception {
        MentorRequest mentorParaSerSalvo = MentorCreator.criarMentorRequest();

        BDDMockito.when(mentorService.salvarMentor(ArgumentMatchers.any(MentorRequest.class)))
            .thenReturn(MentorCreator.criarMentorResponse());

        mockMvc.perform(MockMvcRequestBuilders
            .post("/v1/mentores")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(mentorParaSerSalvo)))
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").hasJsonPath())
            .andExpect(MockMvcResultMatchers.jsonPath("$.nomeCompleto").value(mentorParaSerSalvo.nomeCompleto()));
            
    }

    @Test
    @DisplayName("listarMentores retorna page de mentores quando bem sucedido")
    void listarMentores_RetornaPageDeMentores_QuandoBemSucedido() throws Exception {

        Page<MentorResponse> mentorPageResponse = new PageImpl<>(List.of(MentorCreator.criarMentorResponse()),
        PageRequest.of(0, 10),1);

        BDDMockito.when(mentorService.listarMentores(ArgumentMatchers.any(Pageable.class)))
            .thenReturn(mentorPageResponse);
             
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/mentores"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.content").isNotEmpty())
            .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id").hasJsonPath());
            
    }

    @Test
    @DisplayName("buscarMentorPorId retorna mentor quando bem sucedido")
    void buscarMentorPorId_RetornaMentor_QuandoBemsucedido() throws Exception{
        MentorResponse mentorBuscado = MentorCreator.criarMentorResponse();
        BDDMockito.when(mentorService.buscarMentorPorId(ArgumentMatchers.any(UUID.class)))
            .thenReturn(mentorBuscado);

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/mentores/{id}", mentorBuscado.getId()))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(mentorBuscado.getId().toString()));
    }

    @Test
    @DisplayName("buscarMentoresPorNome retorna page de mentores quando bem sucedido")
    void buscarMentoresPorNome_RetornaPageDeMentores_QuandoBemSucedido() throws Exception{
        String nomeBusca = "a";
        Page<MentorResponse> mentorPageResponse = new PageImpl<>(List.of(MentorCreator.criarMentorResponse()),
        PageRequest.of(0, 10),1);
        BDDMockito.when(mentorService.buscarMentoresPorNome(
            ArgumentMatchers.any(),ArgumentMatchers.any(),ArgumentMatchers.any(),ArgumentMatchers.any(Pageable.class)))
            .thenReturn(mentorPageResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/mentores/find")
            .param("nomeCompleto", nomeBusca))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].nomeCompleto")
                .value(Matchers.containsString(nomeBusca)));
    }

    @Test
    @DisplayName("alterarMentor retorna mentor alterado quando bem sucedido")
    void alterarMentor_RetornaMentorAlterado_QuandoBemSucedido() throws Exception{
        MentorRequest mentorParaSerAlterado = MentorCreator.criarMentorRequest();
        UUID idMentor = MentorCreator.criarMentorResponse().getId();

        BDDMockito.when(mentorService.alterarMentor(ArgumentMatchers.any(UUID.class),
            ArgumentMatchers.any(MentorRequest.class)))
            .thenReturn(MentorCreator.criarMentorResponse());

        mockMvc.perform(MockMvcRequestBuilders.put("/v1/mentores/{id}", idMentor)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(mentorParaSerAlterado)))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(idMentor.toString()));
    }

    @Test
    @DisplayName("deletarMentor remove mentor quando bem sucedido")
    void deletarMentor_RemoveMentor_QuandoBemSucedido() throws Exception{
        BDDMockito.when(mentorRepository.existsById(ArgumentMatchers.any(UUID.class))).thenReturn(true);

        BDDMockito.doNothing().when(mentorRepository).deleteById(ArgumentMatchers.any(UUID.class));

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/mentores/{id}", UUID.randomUUID()))
            .andExpect(MockMvcResultMatchers.status().isNoContent())
            .andExpect(MockMvcResultMatchers.jsonPath("$").doesNotHaveJsonPath());
    }

}
