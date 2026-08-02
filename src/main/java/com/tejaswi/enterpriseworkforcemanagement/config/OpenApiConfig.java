package com.tejaswi.enterpriseworkforcemanagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI(){

        final String securitySchemeName = "Bearer Authentication";

        return new OpenAPI()
                    .info(new Info()
                            .title("Enterprise Workforce Management API")
                            .version("1.0")
                            .description("REST APIs for Enterprise Workforce Management System")
                            
                        )
                    .addSecurityItem(new SecurityRequirement()
                            .addList(securitySchemeName))     
                    
                    .schemaRequirement(
                        securitySchemeName,
                        new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                );
        
    }

}
