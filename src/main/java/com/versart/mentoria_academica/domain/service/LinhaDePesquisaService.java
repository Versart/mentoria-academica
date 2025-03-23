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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LinhaDePesquisaService {

    private final LinhaDePesquisaRepository linhaDePesquisaRepository;

    private final LinhaDePesquisaMapper linhaDePesquisaMapper;

    @Transactional
    public LinhaDePesquisaResponse salvarLinhaDePesquisa(LinhaDePesquisaRequest linhaDePesquisaRequest) {
        if(linhaDePesquisaRepository.existsByNome(linhaDePesquisaRequest.nome())){
            throw new DadoUnicoDuplicadoException("Linha de Pesquisa já cadastrada!");
        }
        var linhaDePesquisa = linhaDePesquisaMapper.toLinhaDePesquisa(linhaDePesquisaRequest);
        var linhaDePesquisaSalva = linhaDePesquisaRepository.save(linhaDePesquisa);
        return linhaDePesquisaMapper.toLinhaDePesquisaResponse(linhaDePesquisaSalva);
    }

    public Page<LinhaDePesquisaResponse> listarLinhasDePesquisa(Pageable pageable) {
        Page<LinhaDePesquisa> linhasDePesquisa = linhaDePesquisaRepository.findAll(pageable);
        return  linhasDePesquisa.map(linhaDePesquisaMapper::toLinhaDePesquisaResponse);
    }

    public LinhaDePesquisaResponse buscarLinhaDePesquisaPorId(UUID id) {
        return linhaDePesquisaRepository.findById(id).map(linhaDePesquisaMapper::toLinhaDePesquisaResponse)
                .orElseThrow(() -> new NaoEncontradoException("Linha de Pesquisa não encontrada"));
    }

    @Transactional
    public LinhaDePesquisaResponse alterarEspecialidade(UUID id, LinhaDePesquisaRequest linhaDePesquisaRequest) {
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
    public void deletarLinhaDePesquisa(UUID id) {
        if(linhaDePesquisaRepository.existsById(id)){
            linhaDePesquisaRepository.deleteById(id);
        }
        else{
            throw new NaoEncontradoException("Linha de Pesquisa não encontrada");
        }
    }

}
