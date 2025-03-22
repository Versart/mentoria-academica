package com.versart.mentoria_academica.domain.repository;

import com.versart.mentoria_academica.domain.model.Departamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DepartamentoRepository extends JpaRepository<Departamento, UUID> {
    boolean existsByNome(String nome);

    Optional<Departamento> findByNome(String nome);

}
