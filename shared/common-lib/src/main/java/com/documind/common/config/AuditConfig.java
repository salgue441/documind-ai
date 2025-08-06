package com.documind.common.config;

import java.util.Optional;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Configuration for JPA auditing to automatically populate createdBy/modifiedBy fields.
 *
 * <p>Integrates with Spring Security to track the current user for audit purposes. Falls back to
 * "system" when no authenticated user is available.
 *
 * @see EnableJpaAuditing
 * @see AuditorAware
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class AuditConfig {

  /**
   * Provides an AuditorAware implementation for JPA auditing.
   *
   * @return AuditorAware bean that retrieves current user from security context
   */
  @Bean
  public AuditorAware<String> auditorProvider() {
    return new SpringSecurityAuditorAware();
  }

  /**
   * AuditorAware implementation that retrieves the current authenticated user from Spring Security
   * context.
   */
  public static class SpringSecurityAuditorAware implements AuditorAware<String> {

    /**
     * Gets the current auditor (user) from security context.
     *
     * @return Optional containing current username or "system" if not authenticated
     */
    @Override
    public Optional<String> getCurrentAuditor() {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

      if (authentication == null
          || !authentication.isAuthenticated()
          || "anonymousUser".equals(authentication.getPrincipal())) {
        return Optional.of("system");
      }

      return Optional.of(authentication.getName());
    }
  }
}
