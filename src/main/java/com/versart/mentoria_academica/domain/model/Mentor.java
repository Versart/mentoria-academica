package com.versart.mentoria_academica.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    private String departamento;
}
