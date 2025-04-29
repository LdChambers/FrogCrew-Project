# Build stage
FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /app

# First, copy pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Then copy actual source code
COPY src ./src

# Now compile
RUN mvn clean package -DskipTests

# Run stage
FROM eclipse-temurin:17-jre
WORKDIR /application

# Copy the built jar
COPY --from=build /app/target/*.jar application.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "application.jar"]
