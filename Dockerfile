# Use Java 21
FROM eclipse-temurin:21-jdk

# Set working directory
WORKDIR /app

# Copy all files
COPY . .

# Build project
RUN chmod +x mvnw && ./mvnw clean package -DskipTests

# Run jar
CMD ["java", "-jar", "target/replaytool-0.0.1-SNAPSHOT.jar"]
