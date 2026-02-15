package com.versart.mentoria_academica.domain.service;

import com.versart.mentoria_academica.api.mapper.MentorMapper;
import com.versart.mentoria_academica.api.model.MentorRequest;
import com.versart.mentoria_academica.api.model.MentorResponse;
import com.versart.mentoria_academica.domain.exception.DadoUnicoDuplicadoException;
import com.versart.mentoria_academica.domain.exception.NaoEncontradoException;
import com.versart.mentoria_academica.domain.model.Mentor;
import com.versart.mentoria_academica.domain.repository.DepartamentoRepository;
import com.versart.mentoria_academica.domain.repository.LinhaDePesquisaRepository;
import com.versart.mentoria_academica.domain.repository.MentorRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class MentorService {

    private final MentorRepository mentorRepository;

    private final DepartamentoRepository departamentoRepository;

    private final LinhaDePesquisaRepository especialidadeRepository;

    private final MentorMapper mentorMapper;

    @Transactional
    @CacheEvict(value = "mentoresPage", allEntries = true)
    public MentorResponse salvarMentor(MentorRequest mentorRequest) {
        log.info("Criando um novo mentor");
        if(mentorRepository.existsByCodigo(mentorRequest.codigo())){
            throw new DadoUnicoDuplicadoException("Código já utilizado");
        }
        if(mentorRepository.existsByEmail(mentorRequest.email())){
            throw new DadoUnicoDuplicadoException("Email já utilizado");
        }
        Mentor mentor = mentorMapper.toMentor(mentorRequest,especialidadeRepository,departamentoRepository);
        Mentor mentorSalvo = mentorRepository.save(mentor);
        return mentorMapper.toMentorResponse(mentorSalvo);
    }

    @Cacheable("mentoresPage")
    public Page<MentorResponse> listarMentores(Pageable pageable) {
        log.info("Buscando todos os mentores");
        Page<Mentor> pageMentores = mentorRepository.findAll(pageable);
        return pageMentores.map(mentorMapper::toMentorResponse);

    }
    @Cacheable(value = "mentores", key = "#id")
    public MentorResponse buscarMentorPorId(UUID id) {
        log.info("Buscando o mentor com o id {}", id);
        return mentorRepository.findById(id).map(mentorMapper::toMentorResponse)
                .orElseThrow(() -> new NaoEncontradoException("Mentor não encontrado"));
    }

    public Page<MentorResponse> buscarMentoresPorNome(String nomeCompleto,String departamento, String linhaDePesquisa, Pageable pageable) {
        log.info("Buscando mentores que contenham {} no nome e departamento {} e " +
        "linha de pesquisa {}", nomeCompleto,departamento,linhaDePesquisa);
        if (nomeCompleto != null && !nomeCompleto.isBlank()) {
            nomeCompleto = "%" + nomeCompleto + "%";
        }
        return mentorRepository.buscaMentoresFiltro(nomeCompleto, departamento,linhaDePesquisa,pageable).map(mentorMapper::toMentorResponse);
        //return mentorRepository.findByNomeCompletoIgnoreCaseContains(nomeCompleto, pageable).map(mentorMapper::toMentorResponse);
    }

    @Transactional
    @CachePut(value = "mentores", key = "#id")
    @CacheEvict(value = "mentoresPage", allEntries = true)
    public MentorResponse alterarMentor(UUID id, MentorRequest mentorRequest) {
        log.info("Alterando o mentor com o id {}", id);
        return mentorRepository.findById(id).map(
                mentor -> {
                    if(!mentor.getCodigo().equalsIgnoreCase(mentorRequest.codigo())
                            && mentorRepository.existsByCodigo(mentorRequest.codigo())){
                        throw new DadoUnicoDuplicadoException("Código já utilizado");

                    }
                    Mentor mentorAlterado = mentorMapper.toMentor(mentorRequest,especialidadeRepository,departamentoRepository);
                    mentorAlterado.setId(id);
                    mentorRepository.save(mentorAlterado);
                    return mentorMapper.toMentorResponse(mentorAlterado);
                }
        ).orElseThrow(() -> new NaoEncontradoException("Mentor não encontrado"));
    }

    @Transactional
    @Caching(evict = {
        @CacheEvict(value = "mentores", key = "#id"),
        @CacheEvict(value = {"mentoresPage"}, allEntries = true)
    })
    public void deletarMentor(UUID id) {
        log.info("Removendo o mentor com o id");
        if(mentorRepository.existsById(id)){
            mentorRepository.deleteById(id);
        }
        else{
            throw new NaoEncontradoException("Mentor não encontrado");
        }
    }

}
