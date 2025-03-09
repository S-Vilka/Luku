FROM openjdk:21

FROM maven:latest

WORKDIR /app

#COPY pom.xml /app/

#COPY . /app/

#CMD ["java", "-jar", "target/LukuLibrary.jar"]

# Copy the JAR file into the Docker image
COPY target/LukuLibrary.jar /app/