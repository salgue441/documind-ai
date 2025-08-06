package com.documind.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI (Swagger) configuration for API documentation.
 *
 * <p>Configures:
 *
 * <ul>
 *   <li>API metadata (title, description, version)
 *   <li>Contact information
 *   <li>License information
 *   <li>JWT authentication scheme
 * </ul>
 *
 * @see OpenAPI
 */
@Configuration
public class OpenApiConfig {

  private static final String JWT_SCHEME_NAME = "bearerAuth";

  /**
   * Creates the OpenAPI configuration bean.
   *
   * @return Configured OpenAPI instance with: - API metadata - Contact information - License - JWT
   *     security scheme
   */
  @Bean
  public OpenAPI docuMindOpenAPI() {
    return new OpenAPI()
        .info(
            new Info()
                .title("DocuMind AI API")
                .description("Intelligent Document Processing Platform")
                .version("1.0.0")
                .contact(
                    new Contact()
                        .name("DocuMind AI Team")
                        .email("support@documind.ai")
                        .url("https://documind.ai"))
                .license(
                    new License().name("MIT License").url("https://opensource.org/licenses/MIT")))
        .components(
            new Components()
                .addSecuritySchemes(
                    JWT_SCHEME_NAME,
                    new SecurityScheme()
                        .name(JWT_SCHEME_NAME)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .in(SecurityScheme.In.HEADER)
                        .description("JWT token for API authentication")))
        .addSecurityItem(new SecurityRequirement().addList(JWT_SCHEME_NAME));
  }
}
