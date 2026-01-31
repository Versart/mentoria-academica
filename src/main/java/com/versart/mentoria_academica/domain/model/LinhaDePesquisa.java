package com.versart.mentoria_academica.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "linhas_de_pesquisa")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class LinhaDePesquisa {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotBlank
    private String nome;

    @ManyToMany(mappedBy = "linhasDePesquisa")
    private Set<Mentor> mentores;
}
