package com.versart.mentoria_academica.api.controller;

import java.util.List;
import java.util.UUID;

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
import com.versart.mentoria_academica.api.model.LinhaDePesquisaRequest;
import com.versart.mentoria_academica.api.model.LinhaDePesquisaResponse;
import com.versart.mentoria_academica.config.SecurityTestConfig;
import com.versart.mentoria_academica.domain.repository.LinhaDePesquisaRepository;
import com.versart.mentoria_academica.domain.service.LinhaDePesquisaService;
import com.versart.mentoria_academica.domain.service.TokenService;
import com.versart.mentoria_academica.util.LinhaDePesquisaCreator;

@WebMvcTest(controllers = LinhaDePesquisaController.class)
@Import({SecurityTestConfig.class})
public class LinhaDePesquisaControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LinhaDePesquisaService linhaDePesquisaService;

    @MockBean
    private TokenService tokenService;

    @MockBean
    private LinhaDePesquisaRepository linhaDePesquisaRepository;


    @Test
    @DisplayName("salvarLinhaDePesquisa retorna linha de pesquisa quando bem sucedido")
    void salvarLinhaDePesquisa_RetornaLinhaDePesquisa_QuandoBemSucedido() throws Exception{
        LinhaDePesquisaRequest linhaDepesquisaParaSerSalva = LinhaDePesquisaCreator.criarLinhaDePesquisaRequest();

        BDDMockito.when(linhaDePesquisaService.salvarLinhaDePesquisa(ArgumentMatchers.any(LinhaDePesquisaRequest.class)))
            .thenReturn(LinhaDePesquisaCreator.criarLinhaDePesquisaResponse());

        mockMvc.perform(MockMvcRequestBuilders
            .post("/v1/linhas-de-pesquisa")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(linhaDepesquisaParaSerSalva)))
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").hasJsonPath())
            .andExpect(MockMvcResultMatchers.jsonPath("$.nome").value(linhaDepesquisaParaSerSalva.nome()));
    }

    @Test
    @DisplayName("listarLinhasDePesquisa retorna page de linhas de pesquisa quando bem sucedido")
    void listarLinhasDePesquisa_RetornaPageDeLinhasDePesquisa_QuandoBemSucedido() throws Exception{
        Page<LinhaDePesquisaResponse> departamentoPageResponse = new PageImpl<>(List.of(LinhaDePesquisaCreator.criarLinhaDePesquisaResponse()),
        PageRequest.of(0, 10),1);

        BDDMockito.when(linhaDePesquisaService.listarLinhasDePesquisa(ArgumentMatchers.any(Pageable.class)))
            .thenReturn(departamentoPageResponse);
             
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/linhas-de-pesquisa"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.content").isNotEmpty())
            .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id").hasJsonPath());
    }

    @Test
    @DisplayName("buscarLinhaDePesquisaPorId retorna linha de pesquisa quando bem sucedido")
    void buscarLinhaDePesquisaPorId_RetornaLinhaDePesquisa_QuandoBemSucedido() throws Exception{
        LinhaDePesquisaResponse linhaDePesquisaBuscada = LinhaDePesquisaCreator.criarLinhaDePesquisaResponse();
        BDDMockito.when(linhaDePesquisaService.buscarLinhaDePesquisaPorId(ArgumentMatchers.any(UUID.class)))
            .thenReturn(linhaDePesquisaBuscada);

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/linhas-de-pesquisa/{id}", linhaDePesquisaBuscada.getId()))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(linhaDePesquisaBuscada.getId().toString()));
    }

    @Test
    @DisplayName("alterarLinhaDePesquisa retorna linha de pesquisa alterada quando bem sucedido")
    void alterarLinhaDePesquisa_RetornaLinhaDePesquisaAlterada_QuandoBemSucedido() throws Exception{
        LinhaDePesquisaRequest linhaDePesquisaParaSerAlterada = LinhaDePesquisaCreator.criarLinhaDePesquisaRequest();
        UUID idLinhaDepesquisa = LinhaDePesquisaCreator.criarLinhaDePesquisaResponse().getId();

        BDDMockito.when(linhaDePesquisaService.alterarLinhaDePesquisa(ArgumentMatchers.any(UUID.class),
            ArgumentMatchers.any(LinhaDePesquisaRequest.class)))
            .thenReturn(LinhaDePesquisaCreator.criarLinhaDePesquisaResponse());

        mockMvc.perform(MockMvcRequestBuilders.put("/v1/linhas-de-pesquisa/{id}", idLinhaDepesquisa)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(linhaDePesquisaParaSerAlterada)))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(idLinhaDepesquisa.toString()));
    }

    @Test
    @DisplayName("deletarLinhaDePesquisa remove linha de pesquisa quando bem sucedido")
    void deletarLinhaDePesquisa_RemoveLinhaDepesquisa_QuandoBemSucedido() throws Exception{
         BDDMockito.when(linhaDePesquisaRepository.existsById(ArgumentMatchers.any(UUID.class))).thenReturn(true);

        BDDMockito.doNothing().when(linhaDePesquisaRepository).deleteById(ArgumentMatchers.any(UUID.class));

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/linhas-de-pesquisa/{id}", UUID.randomUUID()))
            .andExpect(MockMvcResultMatchers.status().isNoContent())
            .andExpect(MockMvcResultMatchers.jsonPath("$").doesNotHaveJsonPath());
    }

   

    
}
