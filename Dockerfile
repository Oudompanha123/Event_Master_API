FROM maven:3.9.7-eclipse-temurin-21 AS build

WORKDIR /app

COPY . .

# Tests load the full Spring context and need the database reachable from the
# build host, so they are skipped here and run separately in CI/locally.
RUN mvn -B clean package -DskipTests

FROM eclipse-temurin:21-jre

WORKDIR /

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "/app.jar"]
