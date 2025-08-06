package com.documind.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Main entry point for the User Service application.
 *
 * <p>This Spring Boot application handles user authentication and management functionalities. It
 * configures component scanning, entity scanning, JPA repositories, and caching.
 *
 * @author Carlos Salguero
 * @version 1.0
 * @since 2025-08-06
 */
@SpringBootApplication(scanBasePackages = {"com.documind.user", "com.documind.common"})
@EntityScan(basePackages = {"com.documind.user.entity", "com.documind.common.entity"})
@EnableJpaRepositories(basePackages = "com.documind.user.repository")
@EnableCaching
public class UserServiceApplication {

  /**
   * Main method that starts the Spring Boot application.
   *
   * @param args command line arguments
   */
  public static void main(String[] args) {
    SpringApplication.run(UserServiceApplication.class, args);
  }
}
