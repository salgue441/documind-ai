package com.documind.user.config;

import java.util.Optional;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * JPA Auditing configuration for the User Service.
 *
 * <p>Provides auditor information for automatic population of createdBy and modifiedBy fields.
 *
 * @author Carlos Salguero
 * @version 1.0
 * @since 2025-08-06
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class UserServiceAuditConfig {

  /**
   * Provides the current auditor for JPA auditing.
   *
   * @return AuditorAware implementation that gets current user from security context
   */
  @Bean
  public AuditorAware<String> auditorProvider() {
    return new UserServiceAuditorAware();
  }

  /** AuditorAware implementation that retrieves the current user from Spring Security context. */
  public static class UserServiceAuditorAware implements AuditorAware<String> {

    @Override
    @NonNull
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
