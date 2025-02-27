package com.versart.mentoria_academica.domain.service;

import com.versart.mentoria_academica.api.model.MentorRequest;
import com.versart.mentoria_academica.api.model.MentorResponse;
import com.versart.mentoria_academica.domain.exception.DadoUnicoDuplicadoException;
import com.versart.mentoria_academica.domain.exception.NaoEncontradoException;
import com.versart.mentoria_academica.domain.model.Departamento;
import com.versart.mentoria_academica.domain.model.Especialidade;
import com.versart.mentoria_academica.domain.model.Mentor;
import com.versart.mentoria_academica.domain.repository.DepartamentoRepository;
import com.versart.mentoria_academica.domain.repository.EspecialidadeRepository;
import com.versart.mentoria_academica.domain.repository.MentorRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MentorService {

    private final MentorRepository mentorRepository;

    private final DepartamentoRepository departamentoRepository;

    private final EspecialidadeRepository especialidadeRepository;

    @Transactional
    public MentorResponse salvarMentor(MentorRequest mentorRequest) {
        if(mentorRepository.existsByCodigo(mentorRequest.codigo())){
            throw new DadoUnicoDuplicadoException("Código já utilizado");
        }
        Departamento departamento = departamentoRepository.findByNome(mentorRequest.departamentoNome()).orElseThrow(
                () -> new NaoEncontradoException("Departamento não encontrado")
        );
        Set<Especialidade> especialidades = new HashSet<>();
        mentorRequest.especialidades().forEach(
                especialidade -> {
                    especialidades.add(especialidadeRepository.findByNome(especialidade).orElseThrow(
                            () -> new NaoEncontradoException("Especialidade não encontrada")));
                }
        );
        var mentor = new Mentor();
        mentor.setDepartamento(departamento);
        mentor.setEspecialidades(especialidades);
        BeanUtils.copyProperties(mentorRequest, mentor);
        Mentor mentorSalvo = mentorRepository.save(mentor);
        MentorResponse mentorResponse = new MentorResponse();
        BeanUtils.copyProperties(mentorSalvo,mentorResponse);
        return mentorResponse;
    }

    public Page<MentorResponse> listarMentores(Pageable pageable) {
        Page<Mentor> pageMentores = mentorRepository.findAll(pageable);
        return pageMentores.map( mentor -> {
            MentorResponse mentorResponse = new MentorResponse();
            BeanUtils.copyProperties(mentor,mentorResponse);
            return mentorResponse;
        });

    }

    public MentorResponse buscarMentorPorId(UUID id) {
        return mentorRepository.findById(id).map(
                mentor -> {
                    MentorResponse mentorResponse = new MentorResponse();
                    BeanUtils.copyProperties(mentor, mentorResponse);
                    return mentorResponse;
                }
        ).orElseThrow(() -> new NaoEncontradoException("Mentor não encontrado"));
    }

    @Transactional
    public MentorResponse alterarMentor(UUID id, MentorRequest mentorRequest) {
        return mentorRepository.findById(id).map(
                mentor -> {
                    BeanUtils.copyProperties(mentorRequest, mentor);
                    MentorResponse mentorResponse = new MentorResponse();
                    BeanUtils.copyProperties(mentor,mentorResponse);
                    return mentorResponse;
                }
        ).orElseThrow(() -> new NaoEncontradoException("Mentor não encontrado"));
    }

    @Transactional
    public void deletarMentor(UUID id) {
        if(mentorRepository.existsById(id)){
            mentorRepository.deleteById(id);
        }
        else{
            throw new NaoEncontradoException("Mentor não encontrado");
        }
    }
}
