package com.versart.mentoria_academica.domain.repository;

import com.versart.mentoria_academica.domain.model.Especialidade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface EspecialidadeRepository extends JpaRepository<Especialidade, UUID> {

    boolean existsByNome(String nome);

    Optional<Especialidade> findByNome(String nome);
}
