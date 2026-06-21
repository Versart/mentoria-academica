create table mentores_especialidades(
    mentor_id uuid,
    especialidade_id uuid,
    foreign key(mentor_id) references mentores(id),
    foreign key (especialidade_id) references especialidades(id),
    primary key(mentor_id,especialidade_id)
)