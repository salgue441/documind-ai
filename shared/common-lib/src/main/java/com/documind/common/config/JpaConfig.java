package com.documind.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * JPA configuration class enabling auditing support.
 *
 * <p>Provides JPA auditing configuration with Spring Security integration for automatic
 * createdBy/updatedBy field population.
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class JpaConfig {
  // JPA auditing is now configured in individual services
  // to avoid bean conflicts and circular dependencies
}
