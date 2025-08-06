package com.documind.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Test configuration class providing common beans for testing scenarios.
 *
 * <p>This configuration is specifically designed for testing environments and provides:
 *
 * <ul>
 *   <li>Password encoder bean for security testing
 *   <li>Other test-specific components
 * </ul>
 *
 * @see BCryptPasswordEncoder
 * @see Configuration
 */
@Configuration
public class TestConfiguration {

  /**
   * Provides a password encoder bean for testing purposes.
   *
   * <p>Uses BCrypt strong hashing algorithm with default strength (10).
   *
   * @return BCryptPasswordEncoder instance configured for testing
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
