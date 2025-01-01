package com.versart.mentoria_academica.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "especialidades")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Especialidade {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String nome;

    @ManyToMany(mappedBy = "especialidades")
    private Set<Mentor> mentores;
}
