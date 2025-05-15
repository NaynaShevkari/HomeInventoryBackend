# Use OpenJDK image
FROM eclipse-temurin:21-jdk-alpine

# Set working directory
WORKDIR /app

# Copy Maven wrapper and project files
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# ✅ Give execute permission to mvnw
RUN chmod +x mvnw

# Prepare dependencies
RUN ./mvnw dependency:go-offline

# Copy the rest of the code
COPY src ./src

# Build the project
RUN ./mvnw clean package -DskipTests

# Run the app
CMD ["java", "-jar", "target/*.jar"]
