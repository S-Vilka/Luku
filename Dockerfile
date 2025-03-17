# Build stage using Maven and Eclipse Temurin (JDK 21)
FROM maven:3.9-eclipse-temurin-21 AS build

# Set the working directory
WORKDIR /app

# Copy Maven project files into the container
COPY pom.xml .
COPY src ./src

# Package the application with dependencies using the maven-shade-plugin
RUN mvn clean package -DskipTests

# Runtime stage using Eclipse Temurin (JDK 21)
FROM eclipse-temurin:21-jdk

# Set noninteractive to avoid prompts
ENV DEBIAN_FRONTEND=noninteractive

# Install necessary dependencies for JavaFX, X11, and Xvfb
RUN apt-get update && \
    apt-get install -y --no-install-recommends \
    libgl1 \
    libgtk-3-0 \
    libx11-6 \
    libxcb1 \
    libxtst6 \
    libxrender1 \
    xauth \
    x11-apps \
    xvfb \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

# Set environment variable for X11 display
ENV DISPLAY=:99

# Set working directory
WORKDIR /app

# Copy the Maven built shaded application (JAR file) from the build stage
COPY --from=build /app/target/LukuLibrary.jar app.jar

### Create the startup script for running the JavaFX app
RUN echo '#!/bin/bash\n\
    # Start JavaFX application with the display set to Xvfb\n\
    java -Dprism.order=sw -Djava.awt.headless=true -Dcom.sun.javafx.headless=true -Djava.library.path=/usr/lib/jvm/java-21-openjdk-amd64/lib -jar app.jar' > /app/start.sh && \
    chmod +x /app/start.sh

# Set the entry point to the startup script
ENTRYPOINT ["/bin/bash", "/app/start.sh"]
