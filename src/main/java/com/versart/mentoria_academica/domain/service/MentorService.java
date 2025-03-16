package com.versart.mentoria_academica.domain.service;

import com.versart.mentoria_academica.api.mapper.MentorMapper;
import com.versart.mentoria_academica.api.model.MentorRequest;
import com.versart.mentoria_academica.api.model.MentorResponse;
import com.versart.mentoria_academica.domain.exception.DadoUnicoDuplicadoException;
import com.versart.mentoria_academica.domain.exception.NaoEncontradoException;
import com.versart.mentoria_academica.domain.model.Mentor;
import com.versart.mentoria_academica.domain.repository.DepartamentoRepository;
import com.versart.mentoria_academica.domain.repository.EspecialidadeRepository;
import com.versart.mentoria_academica.domain.repository.MentorRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MentorService {

    private final MentorRepository mentorRepository;

    private final DepartamentoRepository departamentoRepository;

    private final EspecialidadeRepository especialidadeRepository;

    private final MentorMapper mentorMapper;

    @Transactional
    public MentorResponse salvarMentor(MentorRequest mentorRequest) {
        if(verificarSeCodigoExiste(mentorRequest.codigo())){
            throw new DadoUnicoDuplicadoException("Código já utilizado");
        }
        Mentor mentor = mentorMapper.toMentor(mentorRequest,especialidadeRepository,departamentoRepository);
        Mentor mentorSalvo = mentorRepository.save(mentor);
        MentorResponse mentorResponse = mentorMapper.toMentorResponse(mentorSalvo);
        return mentorResponse;
    }

    public Page<MentorResponse> listarMentores(Pageable pageable) {
        Page<Mentor> pageMentores = mentorRepository.findAll(pageable);
        return pageMentores.map( mentor -> {
            return mentorMapper.toMentorResponse(mentor);
        });

    }

    public MentorResponse buscarMentorPorId(UUID id) {
        return mentorRepository.findById(id).map(
                mentor -> {
                    MentorResponse mentorResponse = new MentorResponse();
                    return mentorMapper.toMentorResponse(mentor);
                }
        ).orElseThrow(() -> new NaoEncontradoException("Mentor não encontrado"));
    }

    @Transactional
    public MentorResponse alterarMentor(UUID id, MentorRequest mentorRequest) {
        if(verificarSeCodigoExiste(mentorRequest.codigo())){
            throw new DadoUnicoDuplicadoException("Código já utilizado");
        }
        return mentorRepository.findById(id).map(
                mentor -> {
                    Mentor mentorAlterado = mentorMapper.toMentor(mentorRequest,especialidadeRepository,departamentoRepository);
                    mentorAlterado.setId(id);
                    mentorRepository.save(mentorAlterado);
                    return mentorMapper.toMentorResponse(mentorAlterado);
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

    private boolean verificarSeCodigoExiste(String codigo) {
        return mentorRepository.existsByCodigo(codigo);
    }
}
