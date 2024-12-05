package com.c0lap5o.JWTAuthenticationBackend.documentation;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for OpenAPI documentation.
 */
@Configuration
public class OpenApiConfig {

    /**
     * Bean to configure the OpenAPI documentation for the JahCloud API.
     *
     * @return An instance of OpenAPI with the configured information.
     */
    @Bean
    public OpenAPI jahCloudOpenAPI() {
        return new OpenAPI()
                // Set the information section of the OpenAPI documentation
                .info(new Info()
                        .title("JahCloud API") // Title of the API
                        .description("API for JahCloud File Management") // Brief description of the API
                        .version("1.0") // Version of the API
                        .contact(new Contact() // Contact information
                                .name("c0lap5o") // Name of the contact
                                .email("pedrocolaco89@gmail.com") // Email of the contact
                                .url("https://c0lap5o.dev")) // URL of the contact
                        .license(new License() // License information
                                .name("Apache 2.0") // Name of the license
                                .url("http://www.apache.org/licenses/LICENSE-2.0.html"))) // URL of the license

                // Define the components section, specifically security schemes
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", // Name of the security scheme
                                new SecurityScheme() // Define the security scheme
                                        .type(SecurityScheme.Type.HTTP) // Type of the security scheme (HTTP)
                                        .scheme("bearer") // Scheme of the security scheme (Bearer)
                                        .bearerFormat("JWT"))); // Format of the Bearer token (JWT)
    }
}