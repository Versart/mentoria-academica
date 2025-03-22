package com.versart.mentoria_academica.domain.service;

import com.versart.mentoria_academica.api.mapper.EspecialidadeMapper;
import com.versart.mentoria_academica.api.model.EspecialidadeRequest;
import com.versart.mentoria_academica.api.model.EspecialidadeResponse;
import com.versart.mentoria_academica.domain.exception.DadoUnicoDuplicadoException;
import com.versart.mentoria_academica.domain.exception.NaoEncontradoException;
import com.versart.mentoria_academica.domain.model.Especialidade;
import com.versart.mentoria_academica.domain.repository.EspecialidadeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EspecialidadeService {

    private final EspecialidadeRepository especialidadeRepository;

    private final EspecialidadeMapper especialidadeMapper;

    @Transactional
    public EspecialidadeResponse salvarEspecialidade(EspecialidadeRequest especialidadeRequest) {
        if(verificarSeNomeExiste(especialidadeRequest.nome())){
            throw new DadoUnicoDuplicadoException("Especialidade já cadastrada!");
        }
        var especialidade = especialidadeMapper.toEspecialidade(especialidadeRequest);
        var especialidadeSalva = especialidadeRepository.save(especialidade);
        return especialidadeMapper.toEspecialidadeResponse(especialidadeSalva);
    }

    public Page<EspecialidadeResponse> listarEspecialidades(Pageable pageable) {
        Page<Especialidade> especialidades = especialidadeRepository.findAll(pageable);
        return  especialidades.map(especialidadeMapper::toEspecialidadeResponse);
    }

    public EspecialidadeResponse buscarEspecialidadePorId(UUID id) {
        return especialidadeRepository.findById(id).map(especialidadeMapper::toEspecialidadeResponse)
                .orElseThrow(() -> new NaoEncontradoException("Especialidade não encontrada"));
    }

    @Transactional
    public EspecialidadeResponse alterarEspecialidade(UUID id, EspecialidadeRequest especialidadeRequest) {
        if(verificarSeNomeExiste(especialidadeRequest.nome())){
            throw new DadoUnicoDuplicadoException("Especialidade já cadastrada!");
        }
        return especialidadeRepository.findById(id).map(
            especialidade -> {
                var especialidadeAlterada = especialidadeMapper.toEspecialidade(especialidadeRequest);
                especialidadeAlterada.setId(id);
                especialidadeRepository.save(especialidadeAlterada);
                return especialidadeMapper.toEspecialidadeResponse(especialidadeAlterada);
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

    private boolean verificarSeNomeExiste(String nome){
        return especialidadeRepository.existsByNome(nome);
    }
}
