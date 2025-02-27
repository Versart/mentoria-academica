package com.versart.mentoria_academica.domain.service;

import com.versart.mentoria_academica.api.model.AlunoRequest;
import com.versart.mentoria_academica.api.model.AlunoResponse;
import com.versart.mentoria_academica.domain.exception.DadoUnicoDuplicadoException;
import com.versart.mentoria_academica.domain.exception.NaoEncontradoException;
import com.versart.mentoria_academica.domain.model.Aluno;
import com.versart.mentoria_academica.domain.repository.AlunoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AlunoService {


    private final AlunoRepository alunoRepository;

    @Transactional
    public AlunoResponse salvarAluno(AlunoRequest alunoRequest) {
        if(alunoRepository.existsByCodigo(alunoRequest.codigo())){
            throw new DadoUnicoDuplicadoException("Código já utilizado");
        }
        var aluno = new Aluno();
        BeanUtils.copyProperties(alunoRequest, aluno);
        Aluno alunoSalvo = alunoRepository.save(aluno);
        AlunoResponse alunoResponse = new AlunoResponse();
        BeanUtils.copyProperties(alunoSalvo,alunoResponse);
        return alunoResponse;
    }

    public Page<AlunoResponse> listarAlunos(Pageable pageable) {
        Page<Aluno> pageAlunos = alunoRepository.findAll(pageable);
        return pageAlunos.map( aluno -> {
            AlunoResponse alunoResponse = new AlunoResponse();
            BeanUtils.copyProperties(aluno,alunoResponse);
            return alunoResponse;
        });

    }

    public AlunoResponse buscarAlunoPorId(UUID id) {
        return alunoRepository.findById(id).map(
                aluno -> {
                    AlunoResponse alunoResponse = new AlunoResponse();
                    BeanUtils.copyProperties(aluno, alunoResponse);
                    return alunoResponse;
                }
        ).orElseThrow(() -> new NaoEncontradoException("Aluno não encontrado"));
    }

    @Transactional
    public AlunoResponse alterarAluno(UUID id, AlunoRequest alunoRequest) {
       return alunoRepository.findById(id).map(
                aluno -> {
                    BeanUtils.copyProperties(alunoRequest, aluno);
                    AlunoResponse alunoResponse = new AlunoResponse();
                    BeanUtils.copyProperties(aluno,alunoResponse);
                    return alunoResponse;
                }
        ).orElseThrow(() -> new NaoEncontradoException("Aluno não encontrado"));
    }

    @Transactional
    public void deletarAluno(UUID id) {
        if(alunoRepository.existsById(id)){
            alunoRepository.deleteById(id);
        }
        else{
            throw new NaoEncontradoException("Aluno não encontrado");
        }
    }


}
