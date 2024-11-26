package com.versart.mentoria_academica.domain.repository;

import com.versart.mentoria_academica.domain.model.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, UUID> {

    boolean existsByCodigo(String codigo);
}
