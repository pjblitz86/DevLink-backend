# 🏗️ Build Stage (Compiles the application)
FROM maven:3.9.6-eclipse-temurin-21 AS build

# Set working directory
WORKDIR /app

# Copy the Maven project files
COPY pom.xml .

# Download dependencies first (for caching optimization)
RUN mvn dependency:go-offline

# Copy the entire project (source code)
COPY . .

# 🔹 Build the application (this generates the JAR)
RUN mvn clean package -DskipTests

# 🚀 Production Image (Minimal JDK for Running the App)
FROM eclipse-temurin:21-jdk-alpine

# Set working directory
WORKDIR /app

# 🔹 Copy the generated JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose application port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
