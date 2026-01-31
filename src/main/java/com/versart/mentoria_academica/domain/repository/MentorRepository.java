package com.versart.mentoria_academica.domain.repository;

import com.versart.mentoria_academica.domain.model.Mentor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MentorRepository extends JpaRepository<Mentor, UUID> {


    boolean existsByCodigo(String codigo);

    boolean existsByEmail(String email);

    Optional<Mentor> findByCodigo(String codigo);

    Page<Mentor> findByNomeCompletoIgnoreCaseContains(String nomeCompleto, Pageable pageable);
}
