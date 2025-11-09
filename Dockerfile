# Use official Java 21 JDK image
FROM eclipse-temurin:21-jdk AS build

# Set working directory
WORKDIR /app

# Copy Maven wrapper and pom
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Give execute permission to mvnw
RUN chmod +x mvnw

# Download dependencies (offline build)
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY src src

# Package app (skip tests for faster build)
RUN ./mvnw clean package -DskipTests


# --------- Runtime Image ---------
FROM eclipse-temurin:21-jdk

WORKDIR /app

# Copy the JAR from build stage
COPY --from=build /app/target/*.jar app.jar

# Expose default Spring Boot port
EXPOSE 8080

# Run Spring Boot JAR
ENTRYPOINT ["java", "-jar", "app.jar"]