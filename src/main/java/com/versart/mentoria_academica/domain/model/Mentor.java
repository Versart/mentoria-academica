package com.versart.mentoria_academica.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "mentores")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Mentor {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String codigo;

    private String nomeCompleto;

    @ManyToOne
    @JoinColumn(name = "departamento_id")
    private Departamento departamento;

    private String descricao;

    @ManyToMany
    @JoinTable(
            name = "mentores_linhas_de_pesquisa",
            joinColumns = @JoinColumn(name = "mentor_id"),
            inverseJoinColumns = @JoinColumn(name = "linha_de_pesquisa_id")
    )
    private Set<LinhaDePesquisa> linhasDePesquisa;


}
