FROM openjdk:21

FROM maven:latest

WORKDIR /app

#COPY pom.xml /app/

#COPY . /app/


# Copy the JAR file into the Docker image
COPY target/LukuLibrary.jar /app/

CMD ["java", "-jar", "LukuLibrary.jar"]
