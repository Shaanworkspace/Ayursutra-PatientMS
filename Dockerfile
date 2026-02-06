# Build stage (Same as yours)
FROM maven:3.9.9-eclipse-temurin-21 AS builder
WORKDIR /app
COPY . .
RUN mvn -B clean package -DskipTests

# Runtime stage (Badlav yahan hai)
# Hum AWS ki official lambda image use karenge
FROM public.ecr.aws/lambda/java:21
WORKDIR /var/task

# JAR ko copy karo. Lambda mein path '${LAMBDA_TASK_ROOT}/lib/' hona chahiye
COPY --from=builder /app/target/*.jar ${LAMBDA_TASK_ROOT}/lib/
CMD [ "com.patientms.StreamLambdaHandler::handleRequest" ]