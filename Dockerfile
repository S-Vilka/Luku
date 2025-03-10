FROM openjdk:21

FROM maven:latest

WORKDIR /app

# Install Xvfb and x11vnc
RUN apt-get update && apt-get install -y xvfb x11vnc

# Copy the JAR file into the Docker image
COPY target/LukuLibrary.jar /app/

# Set the DISPLAY environment variable
ENV DISPLAY=:99

# Set up Xvfb, start the VNC server, and run the application
CMD ["sh", "-c", "Xvfb :99 -screen 0 1024x768x16 & x11vnc -display :99 -forever -usepw & java -Dprism.order=sw -Djava.awt.headless=true -jar LukuLibrary.jar"]
