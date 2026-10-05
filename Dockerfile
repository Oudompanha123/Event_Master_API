FROM maven:3.9.7-eclipse-temurin-21 AS build

WORKDIR /app

# Download dependencies first, so this layer is cached until pom.xml changes
COPY pom.xml .
RUN mvn -B dependency:go-offline

# Then copy the source and build
COPY src ./src

# Tests need a Docker daemon of their own (Testcontainers), which is not
# available inside this build container - the Jenkins 'Test' stage runs them
# before this image is built.
RUN mvn -B clean package -DskipTests

FROM eclipse-temurin:21-jre

WORKDIR /

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "/app.jar"]
