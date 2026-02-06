# Build stage
FROM maven:3.9.9-eclipse-temurin-21 AS builder
WORKDIR /app
COPY . .
RUN mvn -B clean package -DskipTests

# Runtime stage
FROM public.ecr.aws/lambda/java:21
WORKDIR /var/task

# JAR ko seedha /var/task mein copy karo (lib folder me mat dalna)
COPY --from=builder /app/target/PatientMS-0.0.1-SNAPSHOT.jar app.jar

# Lambda ko batana ki ye JAR kahan hai
ENV CLASSPATH="/var/task/app.jar"

# Handler check kar lo
CMD [ "com.patientms.StreamLambdaHandler::handleRequest" ]