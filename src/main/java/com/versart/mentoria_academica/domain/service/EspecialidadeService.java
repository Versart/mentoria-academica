package com.versart.mentoria_academica.domain.service;

import com.versart.mentoria_academica.api.model.EspecialidadeRequest;
import com.versart.mentoria_academica.api.model.EspecialidadeResponse;
import com.versart.mentoria_academica.domain.exception.DadoUnicoDuplicadoException;
import com.versart.mentoria_academica.domain.model.Especialidade;
import com.versart.mentoria_academica.domain.repository.EspecialidadeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

@Service
@RequiredArgsConstructor
public class EspecialidadeService {

    private final EspecialidadeRepository especialidadeRepository;

    public EspecialidadeResponse salvarEspecialidade(EspecialidadeRequest especialidadeRequest) {
        if(especialidadeRepository.existsByNome(especialidadeRequest.nome())){
            throw new DadoUnicoDuplicadoException("Especialidade já cadastrada!");
        }
        var especialidade = new Especialidade();
        BeanUtils.copyProperties(especialidadeRequest, especialidade);
        var especialidadeSalva = especialidadeRepository.save(especialidade);
        EspecialidadeResponse especialidadeResponse = new EspecialidadeResponse();
        BeanUtils.copyProperties(especialidadeSalva, especialidadeResponse);
        return especialidadeResponse;
    }
}
