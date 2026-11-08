package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI proyectoOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("API Proyecto Vehiculos")
                .version("1.0")
                .description("Gestion de vehiculos, documentos, conductores y trayectos (POO II)"))
            .components(new Components()
                .addSecuritySchemes("bearerAuth", new SecurityScheme()
                    .type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT"))
                .addSecuritySchemes("apiKey", new SecurityScheme()
                    .type(SecurityScheme.Type.APIKEY).in(SecurityScheme.In.HEADER).name("APIKey")))
            // Boton "Authorize" de Swagger: token JWT + APIKey
            .addSecurityItem(new SecurityRequirement().addList("bearerAuth").addList("apiKey"));
    }
}
