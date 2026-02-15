package com.versart.mentoria_academica.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "mentores")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
public class Mentor {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Include
    private UUID id;

    @EqualsAndHashCode.Include
    private String codigo;

    private String nomeCompleto;

    @ManyToOne
    @JoinColumn(name = "departamento_id")
    private Departamento departamento;

    private String descricao;

    private String email;

    @ManyToMany
    @JoinTable(
            name = "mentores_linhas_de_pesquisa",
            joinColumns = @JoinColumn(name = "mentor_id"),
            inverseJoinColumns = @JoinColumn(name = "linha_de_pesquisa_id")
    )
    private Set<LinhaDePesquisa> linhasDePesquisa;

    private Boolean disponivel;
}
