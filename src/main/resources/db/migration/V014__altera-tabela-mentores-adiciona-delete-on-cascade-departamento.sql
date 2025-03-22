alter table mentores drop constraint if exists fk_departamento;

alter table mentores add constraint fk_departamento foreign key (departamento_id) references departamentos(id)
on delete cascade;
