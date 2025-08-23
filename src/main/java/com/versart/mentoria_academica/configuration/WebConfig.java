package com.versart.mentoria_academica.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    public void addCorsMappings(CorsRegistry registry) {
        System.out.println("Cors configurado");
         registry.addMapping("/**")
                 .allowedOrigins("*")
                 .allowedHeaders("*")
                 .allowedMethods("GET", "PUT", "POST", "PATCH", "DELETE", "OPTIONS");
    }
}
