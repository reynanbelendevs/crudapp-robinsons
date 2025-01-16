FROM gradle:7.5.0-jdk17 AS build
WORKDIR /app
COPY . .
ENV DOCKER_HOST=tcp://host.docker.internal:23750
ENV TESTCONTAINERS_HOST_OVERRIDE=host.docker.internal 
ENV SERVER_DB_HOST=host.docker.internal
ENV SERVER_REDIS_HOST=host.docker.internal
ENV APP_AUTHOR="REYNAN BELEN"
RUN gradle -no-daemon clean build
# Stage 2: Run the application
FROM openjdk:17.0.1-jdk-slim

WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]