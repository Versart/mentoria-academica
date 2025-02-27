package com.versart.mentoria_academica.domain.service;

import com.versart.mentoria_academica.api.model.EspecialidadeRequest;
import com.versart.mentoria_academica.api.model.EspecialidadeResponse;
import com.versart.mentoria_academica.domain.exception.DadoUnicoDuplicadoException;
import com.versart.mentoria_academica.domain.exception.NaoEncontradoException;
import com.versart.mentoria_academica.domain.model.Especialidade;
import com.versart.mentoria_academica.domain.repository.EspecialidadeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EspecialidadeService {

    private final EspecialidadeRepository especialidadeRepository;

    @Transactional
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

    public Page<EspecialidadeResponse> listarEspecialidades(Pageable pageable) {
        Page<Especialidade> especialidades = especialidadeRepository.findAll(pageable);
        return  especialidades.map(
                especialidade -> {
                    EspecialidadeResponse especialidadeResponse = new EspecialidadeResponse();
                    BeanUtils.copyProperties(especialidade,especialidadeResponse);
                    return  especialidadeResponse;
                }
        );
    }

    public EspecialidadeResponse buscarEspecialidadePorId(UUID id) {
        return especialidadeRepository.findById(id).map(
                especialidade -> {
                    EspecialidadeResponse especialidadeResponse = new EspecialidadeResponse();
                    BeanUtils.copyProperties(especialidade,especialidadeResponse);
                    return especialidadeResponse;
                }
        ).orElseThrow(() -> new NaoEncontradoException("Especialidade não encontrada"));
    }

    @Transactional
    public EspecialidadeResponse alterarEspecialidade(UUID id, EspecialidadeRequest especialidadeRequest) {
        return especialidadeRepository.findById(id).map(
            especialidade -> {
                BeanUtils.copyProperties(especialidadeRequest,especialidade);
                EspecialidadeResponse especialidadeResponse = new EspecialidadeResponse();
                BeanUtils.copyProperties(especialidade, especialidadeResponse);
                return especialidadeResponse;
            }
        ).orElseThrow(() -> new NaoEncontradoException("Especialidade não encontrada"));
    }

    @Transactional
    public void deletarEspecialidade(UUID id) {
        if(especialidadeRepository.existsById(id)){
            especialidadeRepository.deleteById(id);
        }
        else{
            throw new NaoEncontradoException("Especialidade não encontrada");
        }
    }
}
