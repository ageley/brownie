# syntax=docker/dockerfile:1

# --- Build stage: compile and package the Spring Boot jar ---
FROM bellsoft/liberica-openjdk-alpine:26 AS build
WORKDIR /workspace

# Cache dependencies first
COPY gradlew settings.gradle.kts build.gradle.kts ./
COPY gradle gradle
RUN ./gradlew --no-daemon dependencies > /dev/null 2>&1 || true

# Build the application
COPY src src
RUN ./gradlew --no-daemon clean bootJar

# --- Runtime stage: slim JRE image ---
FROM bellsoft/liberica-openjre-alpine:26
WORKDIR /app

# Run as a non-root user
RUN addgroup -S brownie && adduser -S brownie -G brownie

COPY --from=build /workspace/build/libs/*.jar app.jar
USER brownie

# Webhook (push) mode serves on this port; not needed for long polling
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
