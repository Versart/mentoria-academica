package com.versart.mentoria_academica.domain.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.versart.mentoria_academica.api.mapper.DepartamentoMapper;
import com.versart.mentoria_academica.api.model.DepartamentoRequest;
import com.versart.mentoria_academica.api.model.DepartamentoResponse;
import com.versart.mentoria_academica.domain.model.Departamento;
import com.versart.mentoria_academica.domain.repository.DepartamentoRepository;
import com.versart.mentoria_academica.util.DepartamentoCreator;


@ExtendWith(MockitoExtension.class)
class DepartamentoServiceTest {

    @InjectMocks
    private DepartamentoService departamentoService;

    @Mock
    private  DepartamentoRepository departamentoRepository;

    @Mock
    private  DepartamentoMapper departamentoMapper;

    @Test
    @DisplayName("salvarDepartamento retorna departamento quando bem sucedido")
    void salvarDepartamento_RetornaDepartamento_QuandoBemSucedido() {
        BDDMockito.when(departamentoRepository.save(ArgumentMatchers.any(Departamento.class)))
            .thenReturn(DepartamentoCreator.criarDepartamentoComId());
        
        BDDMockito.when(departamentoMapper.toDepartamento(ArgumentMatchers.any(DepartamentoRequest.class)))
            .thenReturn(DepartamentoCreator.criarDepartamentoSemId());
        
        BDDMockito.when(departamentoMapper.toDepartamentoResponse(ArgumentMatchers.any(Departamento.class)))
            .thenReturn(DepartamentoCreator.criarDepartamentoResponse());
        
        DepartamentoRequest departamentoParaSerSalvo = DepartamentoCreator.criarDepartamentoRequest();
        DepartamentoResponse departamentoSalvo = departamentoService.salvarDepartamento(departamentoParaSerSalvo);

        Assertions.assertThat(departamentoSalvo).isNotNull();
        Assertions.assertThat(departamentoSalvo.getId()).isNotNull();
        
    }

    @Test
    @DisplayName("listarDepartamentos retorna page de departamentos quando bem sucedido")
    void listarDepartamentos_RetornaPageDeDepartamentos_QuandoBemSucedido() {
        Page<Departamento> departamentoPage = new PageImpl<>(List.of(DepartamentoCreator.criarDepartamentoComId()));

        BDDMockito.when(departamentoRepository.findAll(ArgumentMatchers.any(PageRequest.class))).thenReturn(departamentoPage);

        BDDMockito.when(departamentoMapper.toDepartamentoResponse(ArgumentMatchers.any(Departamento.class)))
            .thenReturn(DepartamentoCreator.criarDepartamentoResponse());

        Page<DepartamentoResponse> departamentoPageRetornada = departamentoService.listarDepartamentos(PageRequest.of(0, 1));

        Assertions.assertThat(departamentoPageRetornada).isNotNull().isNotEmpty();

        Assertions.assertThat(departamentoPageRetornada.getContent().getFirst()).isNotNull();
    }

    @Test
    @DisplayName("buscarDepartamentoPorId retorna departamento quando bem sucedido")
    void buscarDepartamentoPorId_RetornaDepartamento_QuandoBemSucedido() {
        BDDMockito.when(departamentoRepository.findById(ArgumentMatchers.any(UUID.class))).thenReturn(Optional.of(DepartamentoCreator.criarDepartamentoComId()));
        
        BDDMockito.when(departamentoMapper.toDepartamentoResponse(ArgumentMatchers.any(Departamento.class)))
            .thenReturn(DepartamentoCreator.criarDepartamentoResponse());

        UUID idEsperado = DepartamentoCreator.criarDepartamentoComId().getId();

        DepartamentoResponse departamentoRetornado = departamentoService.buscarDepartamentoPorId(idEsperado);

        Assertions.assertThat(departamentoRetornado).isNotNull();

        Assertions.assertThat(departamentoRetornado.getId()).isEqualTo(idEsperado);
    }

    @Test
    @DisplayName("alterarDepartamento retorna departamento alterado quando bem sucedido")
    void alterarDepartamento_RetornaDepartamentoAlterado_QuandoBemSucedido() {
        UUID idDepartamento = DepartamentoCreator.criarDepartamentoComId().getId();

        DepartamentoRequest departamentoParaSerAlterado = DepartamentoCreator.criarDepartamentoRequest();

        BDDMockito.when(departamentoRepository.findById(ArgumentMatchers.any(UUID.class)))
            .thenReturn(Optional.of(DepartamentoCreator.criarDepartamentoComId()));

        BDDMockito.when(departamentoMapper.toDepartamentoResponse(ArgumentMatchers.any(Departamento.class)))
            .thenReturn(DepartamentoCreator.criarDepartamentoResponse());

        BDDMockito.when(departamentoMapper.toDepartamento(ArgumentMatchers.any(DepartamentoRequest.class)))
            .thenReturn(DepartamentoCreator.criarDepartamentoSemId());
            
        DepartamentoResponse departamentoAlterado = departamentoService.alterarDepartamento(idDepartamento, departamentoParaSerAlterado);

        Assertions.assertThat(departamentoAlterado).isNotNull();

        Assertions.assertThat(departamentoAlterado.getId()).isEqualTo(idDepartamento);

        Assertions.assertThat(departamentoAlterado).hasNoNullFieldsOrProperties();
    }

    @Test
    @DisplayName("deletarDepartamento remove departamento quando bem sucedido")
    void deletarDepartamento_RemoveDepartamento_QuandoBemSucedido() {
        BDDMockito.when(departamentoRepository.existsById(ArgumentMatchers.any(UUID.class))).thenReturn(true);

        BDDMockito.doNothing().when(departamentoRepository).deleteById(ArgumentMatchers.any(UUID.class));

        Assertions.assertThatNoException().isThrownBy(() -> departamentoService.deletarDepartamento(UUID.randomUUID()));
    }

    @Test
    void teste() {
       Assertions.assertThat(false).isTrue();
    }

    
}

    
