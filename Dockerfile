FROM openjdk:21

FROM maven:latest

WORKDIR /app

COPY pom.xml /app/

COPY . /app/

RUN mvn clean package

CMD ["java", "-jar", "target/LukuLibrary.jar"]