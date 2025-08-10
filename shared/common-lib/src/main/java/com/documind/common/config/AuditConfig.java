package com.documind.common.config;

import java.util.Optional;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Configuration for JPA auditing to automatically populate createdBy/modifiedBy fields.
 *
 * <p>This configuration is disabled by default to allow individual services to configure their own
 * auditing without conflicts.
 *
 * @see AuditorAware
 */
@Configuration
public class AuditConfig {

  /**
   * Provides an AuditorAware implementation for JPA auditing. Uses @ConditionalOnMissingBean to
   * avoid bean conflicts. Disabled by default - services should provide their own auditorProvider.
   *
   * @return AuditorAware bean that retrieves current user from security context
   */
  @Bean
  @ConditionalOnMissingBean(name = "auditorProvider")
  public AuditorAware<String> defaultAuditorProvider() {
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
    @NonNull
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
