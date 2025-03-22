package com.versart.mentoria_academica.domain.repository;

import com.versart.mentoria_academica.domain.model.LinhaDePesquisa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface LinhaDePesquisaRepository extends JpaRepository<LinhaDePesquisa, UUID> {

    boolean existsByNome(String nome);

    Optional<LinhaDePesquisa> findByNome(String nome);
}
