package com.versart.mentoria_academica.domain.service;

import com.versart.mentoria_academica.api.mapper.DepartamentoMapper;
import com.versart.mentoria_academica.api.model.DepartamentoRequest;
import com.versart.mentoria_academica.api.model.DepartamentoResponse;
import com.versart.mentoria_academica.domain.exception.DadoUnicoDuplicadoException;
import com.versart.mentoria_academica.domain.exception.NaoEncontradoException;
import com.versart.mentoria_academica.domain.model.Departamento;
import com.versart.mentoria_academica.domain.repository.DepartamentoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class DepartamentoService {

    private final DepartamentoRepository departamentoRepository;

    private final DepartamentoMapper departamentoMapper;

    @Transactional
    public DepartamentoResponse salvarDepartamento(DepartamentoRequest departamentoRequest) {
        log.info("Criando um novo departamento");
        if(departamentoRepository.existsByNome(departamentoRequest.nome())){
            throw new DadoUnicoDuplicadoException("Departamento já cadastrado!");
        }
        var departamento = departamentoMapper.toDepartamento(departamentoRequest);
        var departamentoSalvo = departamentoRepository.save(departamento);
        return departamentoMapper.toDepartamentoResponse(departamentoSalvo);
    }

    public Page<DepartamentoResponse> listarDepartamentos(Pageable pageable) {
        log.info("Buscando todos os departamentos");
        Page<Departamento> departamentos = departamentoRepository.findAll(pageable);
        return  departamentos.map(departamentoMapper::toDepartamentoResponse);
    }

    public DepartamentoResponse buscarDepartamentoPorId(UUID id) {
        log.info("Buscando o departamento com o id {}", id);
        return departamentoRepository.findById(id).map(departamentoMapper::toDepartamentoResponse)
                .orElseThrow(() -> new NaoEncontradoException("Especialidade não encontrada"));
    }

    @Transactional
    public DepartamentoResponse alterarDepartamento(UUID id, DepartamentoRequest departamentoRequest) {
        log.info("Alterando o departamento com o id {}", id);
        return departamentoRepository.findById(id).map(
                departamento -> {
                    if(!departamento.getNome().equalsIgnoreCase(departamentoRequest.nome())
                        && departamentoRepository.existsByNome(departamentoRequest.nome())){
                        throw new DadoUnicoDuplicadoException("Departamento já cadastrado!");
                    }
                    var departamentoAlterado = departamentoMapper.toDepartamento(departamentoRequest);
                    departamentoAlterado.setId(id);
                    departamentoRepository.save(departamentoAlterado);
                    return departamentoMapper.toDepartamentoResponse(departamentoAlterado);
                }
        ).orElseThrow(() -> new NaoEncontradoException("Departamento não encontrado"));
    }

    @Transactional
    public void deletarDepartamento(UUID id) {
        log.info("Removendo o departamento com o id {}", id);
        if(departamentoRepository.existsById(id)){
            departamentoRepository.deleteById(id);
        }
        else{
            throw new NaoEncontradoException("Departamento não encontrado");
        }
    }
}
