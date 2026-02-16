package com.versart.mentoria_academica.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SpringDocConfig {

    
     @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
          .addServersItem(new Server().url("https://mentoria-academica-676779291748.southamerica-east1.run.app/"))
          .components(new Components()
          .addSecuritySchemes("bearer-key",
            new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")));
    }    
}
