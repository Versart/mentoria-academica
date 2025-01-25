package com.versart.mentoria_academica.domain.service;

import com.versart.mentoria_academica.api.model.DepartamentoRequest;
import com.versart.mentoria_academica.api.model.DepartamentoResponse;
import com.versart.mentoria_academica.domain.exception.DadoUnicoDuplicadoException;
import com.versart.mentoria_academica.domain.model.Departamento;
import com.versart.mentoria_academica.domain.repository.DepartamentoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DepartamentoService {

    private final DepartamentoRepository departamentoRepository;

    @Transactional
    public DepartamentoResponse salvarDepartamento(DepartamentoRequest departamentoRequest) {
        if(departamentoRepository.existsByNome(departamentoRequest.nome())){
            throw new DadoUnicoDuplicadoException("Departamento já cadastrada!");
        }
        var departamento = new Departamento();
        BeanUtils.copyProperties(departamentoRequest,departamento);
        var departamentoSalvo = departamentoRepository.save(departamento);
        DepartamentoResponse departamentoResponse = new DepartamentoResponse();
        BeanUtils.copyProperties(departamentoSalvo,departamentoResponse);
        return departamentoResponse;
    }
}
