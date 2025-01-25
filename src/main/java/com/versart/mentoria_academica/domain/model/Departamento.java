package com.versart.mentoria_academica.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "departamentos")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Departamento {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String nome;

    @OneToMany(mappedBy = "departamento")
    private Set<Mentor> mentores;


}
