# 🎓 Mentoria Acadêmica - Backend (TCC)

> API REST (Backend) da aplicação de Mentoria Acadêmica, desenvolvida como Trabalho de Conclusão de Curso (TCC).

Este repositório contém o código-fonte do servidor da plataforma. Foi desenvolvido utilizando **Java e Spring Boot** e é responsável pela persistência na base de dados e todas as regras de negócio centrais do sistema.

---

## 🔗 Repositório do Frontend

**IMPORTANTE:** Este projeto é apenas a camada de Backend (API). Para interagir visualmente com o sistema, é necessário utilizar a interface de utilizador.

👉 **Repositório da Interface (Frontend):** [https://github.com/Versart/mentoria-academica-frontend](https://github.com/Versart/mentoria-academica-frontend)

---

## 🛠️ Tecnologias Utilizadas

A API foi construída utilizando os seguintes frameworks e ferramentas:

* **[Java](https://www.java.com/)** - Linguagem principal de desenvolvimento.
* **[Spring Boot](https://spring.io/projects/spring-boot)** - Framework para a construção da API REST.
* **[Maven](https://maven.apache.org/)** - Gestor de dependências e automação de builds (`pom.xml`, `mvnw`).
* **[Docker](https://www.docker.com/)** - Para contentorização da aplicação (`Dockerfile`).

## ⚙️ Pré-requisitos

Para executar o projeto localmente, vai precisar ter instalado na sua máquina:

* [Java JDK 21](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html)
* [Docker](https://www.docker.com/) para rodar o Postgresql localmente
* [Maven](https://maven.apache.org/) 



## 🚀 Como Executar o Projeto Localmente

1. **Clona o repositório e acesse a pasta:**
   ```bash
   git clone https://github.com/Versart/mentoria-academica.git
   ```
2. **Acesse a pasta do projeto:**
    ```bash
   cd mentoria-academica
   ```
3. **Variáveis de Ambiente / Configuração**
    <br>Aqui, você deve ter um email e uma senha de aplicativo. <br> Para mais informações:
    [Senha app](https://support.google.com/accounts/answer/185833?hl=pt-BR)
   <br> As seguintes variáveis de ambiente são utilizadas pelo projeto:
   <ul>
    <li>EMAIL_USERNAME: Endereço de e-mail remetente utilizado pela aplicação. </li>
    <li>EMAIL_PASSWORD: Senha ou App Password do e-mail. </li>
   </ul>
   
   
4. **Executar a aplicação com o maven:**
    <br> Com o email e a senha de aplicativo altere o comando nas variáveis
    ```bash
    $env:EMAIL_USERNAME="{EMAIL USERNAME}"; $env:EMAIL_PASSWORD="{EMAIL PASSWORD}"; mvn spring-boot:run "-Dspring-boot.run.profiles=dev"
    ```
📝 Documentação da API (Swagger)
A documentação interativa dos endpoints da API pode ser acessada através do endereço:

Swagger UI: http://localhost:8080/swagger-ui.html
