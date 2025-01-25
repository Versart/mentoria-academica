alter table mentores alter column departamento type uuid using departamento::uuid;

alter table mentores rename column departamento to departamento_id;

alter table mentores add constraint fk_departamento foreign key(departamento_id)
references departamentos(id);