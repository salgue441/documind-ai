package com.documind.common.config;

import java.util.Arrays;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web configuration for CORS and other web-related settings.
 *
 * <p>Configures:
 *
 * <ul>
 *   <li>CORS policies for API endpoints
 *   <li>Allowed origins, methods, and headers
 *   <li>Credential support
 *   <li>CORS cache duration
 * </ul>
 *
 * @see WebMvcConfigurer
 * @see CorsConfiguration
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

  /**
   * Configures CORS settings for the application.
   *
   * @return CorsConfigurationSource with: - Allowed origins (localhost and documind.ai domains) -
   *     Standard HTTP methods - Common headers - Credential support - 1 hour cache duration
   */
  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();

    configuration.setAllowedOriginPatterns(
        Arrays.asList("http://localhost:*", "https://documind.ai", "https://*.documind.ai"));

    configuration.setAllowedMethods(
        Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));

    configuration.setAllowedHeaders(
        Arrays.asList(
            "Authorization",
            "Content-Type",
            "X-Requested-With",
            "Accept",
            "Origin",
            "Cache-Control",
            "X-File-Name"));

    configuration.setExposedHeaders(Arrays.asList("Authorization", "Content-Disposition"));

    configuration.setAllowCredentials(true);
    configuration.setMaxAge(3600L);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/api/**", configuration);

    return source;
  }
}
