package com.versart.mentoria_academica.domain.service;

import com.versart.mentoria_academica.api.mapper.LinhaDePesquisaMapper;
import com.versart.mentoria_academica.api.model.LinhaDePesquisaRequest;
import com.versart.mentoria_academica.api.model.LinhaDePesquisaResponse;
import com.versart.mentoria_academica.domain.exception.DadoUnicoDuplicadoException;
import com.versart.mentoria_academica.domain.exception.NaoEncontradoException;
import com.versart.mentoria_academica.domain.model.LinhaDePesquisa;
import com.versart.mentoria_academica.domain.repository.LinhaDePesquisaRepository;
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
public class LinhaDePesquisaService {

    private final LinhaDePesquisaRepository linhaDePesquisaRepository;

    private final LinhaDePesquisaMapper linhaDePesquisaMapper;

    @Transactional
    @CacheEvict(value = "linhasDePesquisaPage", allEntries = true)
    public LinhaDePesquisaResponse salvarLinhaDePesquisa(LinhaDePesquisaRequest linhaDePesquisaRequest) {
        log.info("Criando uma nova linha de pesquisa");
        if(linhaDePesquisaRepository.existsByNome(linhaDePesquisaRequest.nome())){
            throw new DadoUnicoDuplicadoException("Linha de Pesquisa já cadastrada!");
        }
        var linhaDePesquisa = linhaDePesquisaMapper.toLinhaDePesquisa(linhaDePesquisaRequest);
        var linhaDePesquisaSalva = linhaDePesquisaRepository.save(linhaDePesquisa);
        return linhaDePesquisaMapper.toLinhaDePesquisaResponse(linhaDePesquisaSalva);
    }

    @Cacheable("linhasDePesquisaPage")
    public Page<LinhaDePesquisaResponse> listarLinhasDePesquisa(Pageable pageable) {
        log.info("Buscando todas as linhas de pesquisa");
        Page<LinhaDePesquisa> linhasDePesquisa = linhaDePesquisaRepository.findAll(pageable);
        return  linhasDePesquisa.map(linhaDePesquisaMapper::toLinhaDePesquisaResponse);
    }

    @Cacheable(value = "linhasDePesquisa", key = "#id")
    public LinhaDePesquisaResponse buscarLinhaDePesquisaPorId(UUID id) {
        log.info("Buscando a linha de pesquisa com o id {}", id);
        return linhaDePesquisaRepository.findById(id).map(linhaDePesquisaMapper::toLinhaDePesquisaResponse)
                .orElseThrow(() -> new NaoEncontradoException("Linha de Pesquisa não encontrada"));
    }

    @Transactional
    @CachePut(value = "linhasDePesquisa", key = "#id")
    @CacheEvict(value = "linhasDePesquisaPage", allEntries = true)
    public LinhaDePesquisaResponse alterarEspecialidade(UUID id, LinhaDePesquisaRequest linhaDePesquisaRequest) {
        log.info("Alterando a linha de pesquisa com o id {}", id);
        return linhaDePesquisaRepository.findById(id).map(
            linhaDePesquisa -> {
                if(!linhaDePesquisa.getNome().equalsIgnoreCase(linhaDePesquisaRequest.nome())
                        && linhaDePesquisaRepository.existsByNome(linhaDePesquisaRequest.nome())) {
                    throw new DadoUnicoDuplicadoException("Linha de Pesquisa já cadastrada!");
                }
                var linhaDePesquisaAlterada = linhaDePesquisaMapper.toLinhaDePesquisa(linhaDePesquisaRequest);
                linhaDePesquisaAlterada.setId(id);
                linhaDePesquisaRepository.save(linhaDePesquisaAlterada);
                return linhaDePesquisaMapper.toLinhaDePesquisaResponse(linhaDePesquisaAlterada);

            }
        ).orElseThrow(() -> new NaoEncontradoException("Linha de Pesquisa não encontrada"));
    }

    @Transactional
    @Caching(evict = {
        @CacheEvict(value = "linhasDePesquisa", key = "#id"),
        @CacheEvict(value ="linhasDePesquisaPage", allEntries = true )
    })
    public void deletarLinhaDePesquisa(UUID id) {
        log.info("Removendo a linha de pesquisa com o id{}", id);
        if(linhaDePesquisaRepository.existsById(id)){
            linhaDePesquisaRepository.deleteById(id);
        }
        else{
            throw new NaoEncontradoException("Linha de Pesquisa não encontrada");
        }
    }

}
