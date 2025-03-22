alter table mentores_linhas_de_pesquisa drop constraint if exists
mentores_especialidades_mentor_id_fkey;


alter table mentores_linhas_de_pesquisa add constraint mentores_especialidades_mentor_id_fkey
foreign key (mentor_id) references mentores(id) on delete cascade;

alter table mentores_linhas_de_pesquisa drop constraint if exists
mentores_linhas_de_pesquisa_linha_de_pesquisa_id_fkey;

alter table mentores_linhas_de_pesquisa add constraint mentores_linhas_de_pesquisa_linha_de_pesquisa_id_fkey
foreign key (linha_de_pesquisa_id) references linhas_de_pesquisa(id) on delete cascade;
