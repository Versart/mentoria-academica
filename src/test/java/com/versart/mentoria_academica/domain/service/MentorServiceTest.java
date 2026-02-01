package com.versart.mentoria_academica.domain.service;

import static org.mockito.ArgumentMatchers.any;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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
import com.versart.mentoria_academica.api.mapper.MentorMapper;
import com.versart.mentoria_academica.api.model.MentorRequest;
import com.versart.mentoria_academica.api.model.MentorResponse;
import com.versart.mentoria_academica.domain.model.Mentor;
import com.versart.mentoria_academica.domain.repository.DepartamentoRepository;
import com.versart.mentoria_academica.domain.repository.LinhaDePesquisaRepository;
import com.versart.mentoria_academica.domain.repository.MentorRepository;
import com.versart.mentoria_academica.util.MentorCreator;


@ExtendWith(MockitoExtension.class)
public class MentorServiceTest {

    @InjectMocks
    private MentorService mentorService;

    @Mock
    private MentorRepository mentorRepository;

    @Mock
    private DepartamentoRepository departamentoRepository;

    @Mock
    private LinhaDePesquisaRepository especialidadeRepository;

    @Mock
    private MentorMapper mentorMapper;

    @BeforeEach
    void init () {

    }

    @Test
    @DisplayName("salvarMentor retorna mentor quando bem sucedido")
    void salvarMentor_RetornaMentor_QuandoBemSucedido() {
        BDDMockito.when(mentorRepository.save(ArgumentMatchers.any(Mentor.class))).thenReturn(MentorCreator.criarMentorComId());

        BDDMockito.when(mentorMapper.toMentor(ArgumentMatchers.any(MentorRequest.class), any(), any()))
            .thenReturn(MentorCreator.criarMentorSemId());

        BDDMockito.when(mentorMapper.toMentorResponse(ArgumentMatchers.any(Mentor.class))).thenReturn(MentorCreator.criarMentorResponse());

        MentorRequest mentorParaSerSalvo = MentorCreator.criarMentorRequest();
        
        MentorResponse mentorSalvo = mentorService.salvarMentor(mentorParaSerSalvo);

        Assertions.assertThat(mentorSalvo).isNotNull();

        Assertions.assertThat(mentorSalvo.getId()).isNotNull();

    }

    @Test
    @DisplayName("listarMentores retorna page de mentores quando bem sucedido")
    void listarMentores_RetornaPageDeMentores_QuandoBemSucedido() {
        Page<Mentor> mentorPage = new PageImpl<>(List.of(MentorCreator.criarMentorComId()));
        
        BDDMockito.when(mentorRepository.findAll(ArgumentMatchers.any(PageRequest.class))).thenReturn(mentorPage);

        BDDMockito.when(mentorMapper.toMentorResponse(ArgumentMatchers.any(Mentor.class))).thenReturn(MentorCreator.criarMentorResponse());

        Page<MentorResponse> mentorPageRetornada = mentorService.listarMentores(PageRequest.of(0, 1));

        Assertions.assertThat(mentorPageRetornada).isNotNull().isNotEmpty();

        Assertions.assertThat(mentorPageRetornada.getContent().getFirst()).isNotNull();
    }

    @Test
    @DisplayName("buscarMentorPorId retorna mentor quando bem sucedido")
    void buscarMentorPorId_RetornaMentor_QuandoBemSucedido() {
        BDDMockito.when(mentorRepository.findById(ArgumentMatchers.any(UUID.class))).thenReturn(Optional.of(MentorCreator.criarMentorComId()));
        
        BDDMockito.when(mentorMapper.toMentorResponse(ArgumentMatchers.any(Mentor.class))).thenReturn(MentorCreator.criarMentorResponse());

        UUID idEsperado = MentorCreator.criarMentorComId().getId();

        MentorResponse mentorRetornado = mentorService.buscarMentorPorId(idEsperado);

        Assertions.assertThat(mentorRetornado).isNotNull();

        Assertions.assertThat(mentorRetornado.getId()).isEqualTo(idEsperado);

    }

    @Test
    @DisplayName("buscarMentoresPorNome retorna page de mentores quando bem sucedido")
    void buscarMentoresPorNome_RetornaPageDeMentores_QuandoBemSucedido() {
        String nomeBuscado = MentorCreator.criarMentorRequest().nomeCompleto();

        Page<Mentor> mentorPorNome = new PageImpl<>(List.of(MentorCreator.criarMentorComId()));

        BDDMockito.when(mentorRepository.findByNomeCompletoIgnoreCaseContains(ArgumentMatchers.anyString(),
            ArgumentMatchers.any(PageRequest.class))).thenReturn(mentorPorNome);
        
        BDDMockito.when(mentorMapper.toMentorResponse(ArgumentMatchers.any(Mentor.class))).thenReturn(MentorCreator.criarMentorResponse());

        Page<MentorResponse> mentorPageRetornada = mentorService.buscarMentoresPorNome(nomeBuscado, PageRequest.of(0, 1));

        Assertions.assertThat(mentorPageRetornada).isNotNull().isNotEmpty();

        Assertions.assertThat(mentorPorNome.getContent().getFirst().getNomeCompleto()).contains(nomeBuscado);
    }


    @Test
    @DisplayName("alterarMentor retorna mentor alterado quando bem sucedido")
    void alterarMentor_RetornaMentorAlterado_QuandoBemSucedido() {
        UUID idMentor = MentorCreator.criarMentorComId().getId();

        MentorRequest mentorParaSerAlterado = MentorCreator.criarMentorRequest();

        BDDMockito.when(mentorRepository.findById(ArgumentMatchers.any(UUID.class))).thenReturn(Optional.of(MentorCreator.criarMentorComId()));

        BDDMockito.when(mentorMapper.toMentorResponse(ArgumentMatchers.any(Mentor.class))).thenReturn(MentorCreator.criarMentorResponse());

        BDDMockito.when(mentorMapper.toMentor(ArgumentMatchers.any(MentorRequest.class), any(), any()))
            .thenReturn(MentorCreator.criarMentorSemId());
            
        MentorResponse mentorAlterado = mentorService.alterarMentor(idMentor, mentorParaSerAlterado);

        Assertions.assertThat(mentorAlterado).isNotNull();

        Assertions.assertThat(mentorAlterado.getId()).isEqualTo(idMentor);

        Assertions.assertThat(mentorAlterado).hasNoNullFieldsOrProperties();

    }
    
    @Test
    void deletarMentor_RemoveMentor_QuandoBemSucedido() {
        BDDMockito.when(mentorRepository.existsById(ArgumentMatchers.any(UUID.class))).thenReturn(true);

        BDDMockito.doNothing().when(mentorRepository).deleteById(ArgumentMatchers.any(UUID.class));

        Assertions.assertThatNoException().isThrownBy(() -> mentorService.deletarMentor(UUID.randomUUID()));

    }
    

  

    

   

   

    
}
