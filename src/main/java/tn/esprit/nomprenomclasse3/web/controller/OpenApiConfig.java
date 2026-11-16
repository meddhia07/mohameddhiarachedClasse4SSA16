package tn.esprit.nomprenomclasse3.web.controller;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Metadonnees de la documentation OpenAPI generee par SpringDoc.
 * Description JSON : /api/v3/api-docs ; Swagger UI : /api/swagger-ui.html
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI autolocOpenApi() {
        return new OpenAPI().info(new Info()
                .title("AutoLoc API")
                .description("API REST de gestion de location de véhicules (ASI 4SSA16 - Atelier 5)")
                .version("1.0.0"));
    }
}
