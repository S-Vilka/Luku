FROM openjdk:21

# Set the working directory
WORKDIR /app

# Copy the JAR file into the container
COPY target/LukuLibrary.jar app.jar

# Command to run the application
CMD ["java", "-jar", "app.jar"]