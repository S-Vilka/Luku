# Library Management System

## Overview
This project is a Library Management System implemented in Java using Maven. It provides functionalities to manage books, authors, reservations, and users in a library.

## Features
- Manage books (add, update, search)
- Manage authors (add, update, delete, search)
- Manage reservations (create, update)
- Manage users (register, update)
- Extend due dates for reservations
- Search for books, authors, and users
- View reservations for a specific user
- Notify users about due dates
- User can reserve limited books based on their role.

## Technologies Used
- Java
- Maven
- MariaDB
- JUnit 5
- Mockito
- Testcontainers
-  Docker
- Jenkins

## Prerequisites
- Java 11 or higher
- Maven 3.6 or higher
- MariaDB
- Docker
- XQuartz (for macOS)

## Shade Plugin
The project uses the Maven Shade Plugin to create an executable JAR file with all dependencies included. 
The plugin is configured in the `pom.xml` file to run JavaFX applications.

   ```sh
     java -jar target/LukuLibrary.jar
   ```

## Setup and Installation

1. **Clone the repository:**
    ```sh
    git clone https://github.com/MahnoorFatima02/library-management-system.git
    cd library-management-system
    ```

2. **Configure the database:**

    - Ensure MariaDB is installed and running.
    - Create a database named `library_db`.

3. **Install sample data:**

    - Run the `SampleDataInserter` file to insert sample data into the database.

4. **Build the project:**
    ```sh
    mvn clean install
    ```

5. **Run the application:**
    ```sh
    mvn exec:java
    ```
   or

    Start the ** Main class** in the view package.

## Running Tests
The project includes unit tests for the service and controller layers. To run the tests, use the following command:
   ```sh
      mvn test
   ```

## Project Structure
- src/main/java: Contains the main application code.
- src/test/java: Contains the test code.
- src/main/resources: Contains configuration files.
- src/main/docker: Contains Dockerfile for building a Docker image.
- Jenkinsfile: Contains the Jenkins pipeline configuration.
- SampleDataInserter.java: Inserts sample data into the database.
- pom.xml: Contains the Maven project configuration.
- Dockerfile: Contains the Docker image configuration.

## Docker Setup
**Build Docker Image:**
   
   ```sh
      docker build -t mahnoor95/luku:v1 .
   ```
** check docker image:**
   
   ```sh
      docker images
   ```

**Run Docker Container:**

```sh
   docker run -p 8080:8080 mahnoor95/luku:v1
```

## Jenkins Setup
**Install Jenkins:**
   Download and install Jenkins from the official website.

**Configure Jenkins:**

- Install necessary plugins (e.g., Docker, Maven).
- Set up credentials for Docker Hub.
- Create Jenkins Pipeline:
- Use the provided Jenkinsfile to create a pipeline job in Jenkins.

## Push to Docker Hub
Push to docker hub through Jenkins pipeline.
**Docker Hub Repository:**
   [Docker Hub](https://hub.docker.com/repository/docker/mahnoor95/luku)

## Docker Play with Jenkins
**Pull Docker Image:**

```sh
  docker pull mahnoor95/luku:v1
```

**Run Docker Container locally:**

```sh
  docker pull --platform amd64 mahnoor95/luku:v1
  
  docker run -it mahnoor95/luku:v1
```