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
import com.versart.mentoria_academica.api.model.DepartamentoRequest;
import com.versart.mentoria_academica.api.model.DepartamentoResponse;
import com.versart.mentoria_academica.config.SecurityTestConfig;
import com.versart.mentoria_academica.domain.repository.DepartamentoRepository;
import com.versart.mentoria_academica.domain.service.DepartamentoService;
import com.versart.mentoria_academica.domain.service.TokenService;
import com.versart.mentoria_academica.util.DepartamentoCreator;


@WebMvcTest(controllers = DepartamentoController.class)
@Import({SecurityTestConfig.class})
public class DepartamentoControllerTest {

    @MockBean
    private DepartamentoRepository departamentoRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    
    @MockBean
    private DepartamentoService departamentoService;

    @MockBean
    private TokenService tokenService;

    @Test
    @DisplayName("salvarDepartamento retorna departamento quando bem sucedido")
    void salvarDepartamento_RetornaMentor_QuandoBemSucedido() throws Exception{
        DepartamentoRequest departamentoParaSerSalvo = DepartamentoCreator.criarDepartamentoRequest();

        BDDMockito.when(departamentoService.salvarDepartamento(ArgumentMatchers.any(DepartamentoRequest.class)))
            .thenReturn(DepartamentoCreator.criarDepartamentoResponse());

        mockMvc.perform(MockMvcRequestBuilders
            .post("/v1/departamentos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(departamentoParaSerSalvo)))
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").hasJsonPath())
            .andExpect(MockMvcResultMatchers.jsonPath("$.nome").value(departamentoParaSerSalvo.nome()));
    }

    @Test
    @DisplayName("listarDepartamentos retorna page de de departamentos quando bem sucedido")
    void listarDepartamentos_RetornaPageDeDepartamentos_QuandoBemSucedido() throws Exception{
        Page<DepartamentoResponse> departamentoPageResponse = new PageImpl<>(List.of(DepartamentoCreator.criarDepartamentoResponse()),
        PageRequest.of(0, 10),1);

        BDDMockito.when(departamentoService.listarDepartamentos(ArgumentMatchers.any(Pageable.class)))
            .thenReturn(departamentoPageResponse);
             
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/departamentos"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.content").isNotEmpty())
            .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id").hasJsonPath());
    }

    @Test
    @DisplayName("buscarDepartamentoPorId retorna departamento quando bem sucedido")
    void buscarDepartamentoPorId_RetornaDepartamento_QuandoBemSucedido() throws Exception{
        DepartamentoResponse departamentoBuscado = DepartamentoCreator.criarDepartamentoResponse();
        BDDMockito.when(departamentoService.buscarDepartamentoPorId(ArgumentMatchers.any(UUID.class)))
            .thenReturn(departamentoBuscado);

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/departamentos/{id}", departamentoBuscado.getId()))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(departamentoBuscado.getId().toString()));
    }

    @Test
    @DisplayName("alterarDepartamento retorna departamento alterado quando bem sucedido")
    void alterarDepartamento_RetornaDepartamentoAlterado_QuandoBemSucedido() throws Exception{
        DepartamentoRequest departamentoParaSerAlterado = DepartamentoCreator.criarDepartamentoRequest();
        UUID idDepartamento = DepartamentoCreator.criarDepartamentoResponse().getId();

        BDDMockito.when(departamentoService.alterarDepartamento(ArgumentMatchers.any(UUID.class),
            ArgumentMatchers.any(DepartamentoRequest.class)))
            .thenReturn(DepartamentoCreator.criarDepartamentoResponse());

        mockMvc.perform(MockMvcRequestBuilders.put("/v1/departamentos/{id}", idDepartamento)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(departamentoParaSerAlterado)))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(idDepartamento.toString()));
    }

   

    @Test
    @DisplayName("deletarDepartamento remove departamento quando bem sucedido")
    void deletarDepartamento_RemoveDepartamento_QuandoBemSucedido() throws Exception{
        BDDMockito.when(departamentoRepository.existsById(ArgumentMatchers.any(UUID.class))).thenReturn(true);

        BDDMockito.doNothing().when(departamentoRepository).deleteById(ArgumentMatchers.any(UUID.class));

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/departamentos/{id}", UUID.randomUUID()))
            .andExpect(MockMvcResultMatchers.status().isNoContent())
            .andExpect(MockMvcResultMatchers.jsonPath("$").doesNotHaveJsonPath());
    }



    

   
}
