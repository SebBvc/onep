ARG SPRING_PROFILES_ACTIVE=prod
# --- Build stage ---
FROM gradle:9.2.1-jdk25 AS builder

WORKDIR /home/gradle/backend

COPY ./build.gradle ./settings.gradle /home/gradle/backend/
# Pre-download dependencies (this layer is cached unless build files change)
RUN gradle --no-daemon --parallel dependencies --configuration runtimeClasspath || true

# Now copy sources (this invalidates only the source layer, not the dependencies layer)
COPY ./src/main /home/gradle/backend/src/main

RUN gradle build --no-daemon --parallel -x test --stacktrace

# --- Runtime stage ---
FROM eclipse-temurin:25-jre
WORKDIR /app

COPY --from=builder /home/gradle/backend/build/libs/*.jar app.jar

EXPOSE 8080
# Use a shell entrypoint so we can expand the env var into the java command
ENTRYPOINT ["sh", "-c", "exec java -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE} -jar /app/app.jar"]
