package com.versart.mentoria_academica.domain.repository;

import com.versart.mentoria_academica.domain.model.Mentor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface MentorRepository extends JpaRepository<Mentor, UUID> {


    boolean existsByCodigo(String codigo);

    boolean existsByEmail(String email);

    Optional<Mentor> findByCodigo(String codigo);

    Page<Mentor> findByNomeCompletoIgnoreCaseContains(String nomeCompleto, Pageable pageable);

     @Query("""
       select distinct m
       from Mentor m
       join m.departamento d
       join m.linhasDePesquisa l
       where (:departamento is null or lower(d.nome) = lower(cast(:departamento as string)))
       and (:linha is null or lower(l.nome) = lower(cast(:linha as string)))
       and (:nome is null or lower(m.nomeCompleto) like lower(cast(:nome as string)))
       """)
    Page<Mentor> buscaMentoresFiltro(@Param("nome")String nome,@Param("departamento")String departamentoNome, @Param("linha")String linhasDePesquisaNome, 
     Pageable pageable);
}
