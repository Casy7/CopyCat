# ==========================================
# STAGE 1: Build (Builder)
# ==========================================
# Use Maven image with Java 21 (Temurin)
FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /app

# Copy the dependency file
COPY pom.xml .
# Download all libraries (cached to speed up subsequent builds)
RUN mvn dependency:go-offline

# Copy all source code
COPY src ./src
# Build the project into a .jar file (skipping tests for speed)
RUN mvn clean package -DskipTests

# ==========================================
# STAGE 2: Run (Runner)
# ==========================================
# Use a lightweight Alpine Linux image with JRE (no Maven)
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Create a folder for uploaded files
RUN mkdir uploads

# Copy the built .jar file from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Expose port 8080 to the world
EXPOSE 8080

# Command that starts the server
ENTRYPOINT ["java", "-jar", "app.jar"]