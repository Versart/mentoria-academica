create table mentores(
    id uuid primary key,
    codigo varchar(20) not null unique,
    nome_completo varchar(255)  not null,
    departamento varchar(50) not null
)