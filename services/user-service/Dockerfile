FROM openjdk:21-jdk-slim

# Set working directory
WORKDIR /app

# Copy Maven wrapper and pom files
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
COPY services/user-service/pom.xml ./services/user-service/
COPY shared/common-lib/pom.xml ./shared/common-lib/

# Download dependencies
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY shared/ ./shared/
COPY services/user-service/src/ ./services/user-service/src/

# Build application
RUN ./mvnw clean package -pl services/user-service -am -DskipTests

# Create final image
FROM openjdk:21-jdk-slim

WORKDIR /app

# Copy jar file
COPY --from=0 /app/services/user-service/target/*.jar app.jar

# Create non-root user
RUN useradd -r -u 1000 -g root documind
USER documind

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s \
  CMD curl -f http://localhost:8081/actuator/health || exit 1

# Expose port
EXPOSE 8081

# Run application
ENTRYPOINT ["java", "-jar", "app.jar"]
