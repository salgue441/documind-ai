package com.documind.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * JPA configuration class enabling auditing support.
 *
 * <p>Note: The actual auditor provider is configured in {@link AuditConfig}. This class exists to
 * enable JPA auditing at the repository level.
 *
 * @see EnableJpaAuditing
 * @see AuditConfig
 */
@Configuration
@EnableJpaAuditing
public class JpaConfig {}
