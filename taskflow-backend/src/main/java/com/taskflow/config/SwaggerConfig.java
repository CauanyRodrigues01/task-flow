package com.taskflow.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API TASKFLOW - Accenture")
                        .version("v1.0.0")
                        .description("API REST para gerenciamento do projeto TaskFlow (CRUD), "
                                + "desenvolvida com Spring Boot, JPA e MySQL e o BMAD-Method. "
                                + "Foco em código com propósito e documentação clara.")
                        .contact(new Contact()
                                .name("Cauany Rodrigues, Vinicius Leal, Kevin Kennedy e João Vitor")
                        )
                )
                .addSecurityItem(new SecurityRequirement().addList("BearerAuth"))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("BearerAuth",
                                new SecurityScheme()
                                        .name("Authorization")
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Informe o token JWT no formato: Bearer {seu_token}")
                        )
                );
    }
}
