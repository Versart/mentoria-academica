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

import com.versart.mentoria_academica.api.mapper.LinhaDePesquisaMapper;
import com.versart.mentoria_academica.api.model.LinhaDePesquisaRequest;
import com.versart.mentoria_academica.api.model.LinhaDePesquisaResponse;
import com.versart.mentoria_academica.domain.model.LinhaDePesquisa;
import com.versart.mentoria_academica.domain.repository.LinhaDePesquisaRepository;
import com.versart.mentoria_academica.util.LinhaDePesquisaCreator;

@ExtendWith(MockitoExtension.class)
public class LinhaDePesquisaServiceTest {
    
    @InjectMocks
    private LinhaDePesquisaService linhaDePesquisaService;

    @Mock
    private  LinhaDePesquisaRepository linhaDePesquisaRepository;

    @Mock
    private  LinhaDePesquisaMapper linhaDePesquisaMapper;

    @Test
    @DisplayName("salvarLinhaDePesquisa retorna linha de pesquisa quando bem sucedido")
    void salvarLinhaDePesquisa_RetornaLinhaDePesquisa_QuandoBemSucedido() {
         BDDMockito.when(linhaDePesquisaRepository.save(ArgumentMatchers.any(LinhaDePesquisa.class)))
            .thenReturn(LinhaDePesquisaCreator.criarLinhaDePesquisaComId());
        
        BDDMockito.when(linhaDePesquisaMapper.toLinhaDePesquisa(ArgumentMatchers.any(LinhaDePesquisaRequest.class)))
            .thenReturn(LinhaDePesquisaCreator.criarLinhaDePesquisaSemId());
        
        BDDMockito.when(linhaDePesquisaMapper.toLinhaDePesquisaResponse(ArgumentMatchers.any(LinhaDePesquisa.class)))
            .thenReturn(LinhaDePesquisaCreator.criarLinhaDePesquisaResponse());
        
        LinhaDePesquisaRequest linhaDePesquisaParaSerSalva = LinhaDePesquisaCreator.criarLinhaDePesquisaRequest();
        LinhaDePesquisaResponse linhaDePesquisaSalva = linhaDePesquisaService.salvarLinhaDePesquisa(linhaDePesquisaParaSerSalva);

        Assertions.assertThat(linhaDePesquisaSalva).isNotNull();
        Assertions.assertThat(linhaDePesquisaSalva.getId()).isNotNull();
    }

    @Test
    @DisplayName("listarLinhasDePesquisa retorna page de linhas de pesquisa quando bem sucedido")
    void listarLinhasDePesquisa_RetornaPageDeLinhasDePesquisa_QuandoBemSucedido() {
        Page<LinhaDePesquisa> linhaDePesquisaPage = new PageImpl<>(List.of(LinhaDePesquisaCreator.criarLinhaDePesquisaComId()));

        BDDMockito.when(linhaDePesquisaRepository.findAll(ArgumentMatchers.any(PageRequest.class))).thenReturn(linhaDePesquisaPage);

        BDDMockito.when(linhaDePesquisaMapper.toLinhaDePesquisaResponse(ArgumentMatchers.any(LinhaDePesquisa.class)))
            .thenReturn(LinhaDePesquisaCreator.criarLinhaDePesquisaResponse());

        Page<LinhaDePesquisaResponse> linhaDePesquisaPageRetornada = linhaDePesquisaService.listarLinhasDePesquisa(PageRequest.of(0, 1));

        Assertions.assertThat(linhaDePesquisaPageRetornada).isNotNull().isNotEmpty();

        Assertions.assertThat(linhaDePesquisaPageRetornada.getContent().getFirst()).isNotNull();
    }

    @Test
    @DisplayName("buscarLinhaDePesquisaPorId retorna linha de pesquisa quando bem sucedido")
    void buscarLinhaDePesquisaPorId() {
        BDDMockito.when(linhaDePesquisaRepository.findById(ArgumentMatchers.any(UUID.class)))
            .thenReturn(Optional.of(LinhaDePesquisaCreator.criarLinhaDePesquisaComId()));
        
        BDDMockito.when(linhaDePesquisaMapper.toLinhaDePesquisaResponse(ArgumentMatchers.any(LinhaDePesquisa.class)))
            .thenReturn(LinhaDePesquisaCreator.criarLinhaDePesquisaResponse());

        UUID idEsperado = LinhaDePesquisaCreator.criarLinhaDePesquisaComId().getId();

        LinhaDePesquisaResponse linhaDePesquisaRetornada = linhaDePesquisaService.buscarLinhaDePesquisaPorId(idEsperado);

        Assertions.assertThat(linhaDePesquisaRetornada).isNotNull();

        Assertions.assertThat(linhaDePesquisaRetornada.getId()).isEqualTo(idEsperado);
    }

    @Test
    @DisplayName("alterarLinhaDePesquisa retorna linha de pesquisa alterada quando bem sucedido")
    void alterarLinhaDePesquisa_RetornaLinhaDePesquisaAlterada_QuandoBemSucedido() {
        UUID idLinhaDepesquisa = LinhaDePesquisaCreator.criarLinhaDePesquisaComId().getId();

        LinhaDePesquisaRequest linhaDePesquisaParaSerAlterada = LinhaDePesquisaCreator.criarLinhaDePesquisaRequest();

        BDDMockito.when(linhaDePesquisaRepository.findById(ArgumentMatchers.any(UUID.class)))
            .thenReturn(Optional.of(LinhaDePesquisaCreator.criarLinhaDePesquisaComId()));

        BDDMockito.when(linhaDePesquisaMapper.toLinhaDePesquisaResponse(ArgumentMatchers.any(LinhaDePesquisa.class)))
            .thenReturn(LinhaDePesquisaCreator.criarLinhaDePesquisaResponse());

        BDDMockito.when(linhaDePesquisaMapper.toLinhaDePesquisa(ArgumentMatchers.any(LinhaDePesquisaRequest.class)))
            .thenReturn(LinhaDePesquisaCreator.criarLinhaDePesquisaSemId());
            
        LinhaDePesquisaResponse linhaDePesquisaAlterada = linhaDePesquisaService.alterarLinhaDePesquisa(idLinhaDepesquisa, linhaDePesquisaParaSerAlterada);

        Assertions.assertThat(linhaDePesquisaAlterada).isNotNull();

        Assertions.assertThat(linhaDePesquisaAlterada.getId()).isEqualTo(idLinhaDepesquisa);

        Assertions.assertThat(linhaDePesquisaAlterada).hasNoNullFieldsOrProperties();
    }

    @Test
    @DisplayName("deletarLinhaDePesquisa remove linha de pesquisa quando bem sucedido")
    void deletarLinhaDePesquisa_RemoveLinhaDepesquisa_QuandoBemSucedido() {
        BDDMockito.when(linhaDePesquisaRepository.existsById(ArgumentMatchers.any(UUID.class))).thenReturn(true);

        BDDMockito.doNothing().when(linhaDePesquisaRepository).deleteById(ArgumentMatchers.any(UUID.class));

        Assertions.assertThatNoException().isThrownBy(() -> linhaDePesquisaService.deletarLinhaDePesquisa(UUID.randomUUID()));
    }

    

    
}
