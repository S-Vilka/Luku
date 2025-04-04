# Library Management System

## Overview
Luku is a Library Management System developed for students and teachers, offering intuitive features to browse, search, and reserve books. Users can search by category, author, or title, and manage their reservations with ease.

The project is implemented in Java using Maven and supports localization in English, Russian, and Urdu.

Luku was developed as part of a second-year Software Engineering course at Metropolia University of Applied Sciences.

## Features
- Manage reservations (create, update)
- Manage users (register, update)
- Extend due dates for reservations
- Search for books, authors, and users
- View reservations for a specific user
- Notify users about due dates
- User can reserve limited books based on their role.
- User can extend the due date of the book.
- User can view the list of books they have reserved.
- User can view the list of books they have borrowed.
- Notification is sent to the user when the due date is near.
- User can choose from three user interface languages: English, Russian, and Urdu.

## Technologies Used
- Java
- Maven
- MariaDB
- JUnit 5
- Mockito
- Testcontainers
- Docker
- Jenkins

## Prerequisites
- Java 11 or higher
- Maven 3.6 or higher
- MariaDB
- Docker
- XQuartz (for macOS)


## Use Case Diagram
![Screenshot 2025-03-19 at 20 30 10](https://github.com/user-attachments/assets/0e5b8b43-27ef-4387-b4ca-8f5a2032cb64)

## Class Diagram
![Screenshot 2025-03-19 at 20 29 14](https://github.com/user-attachments/assets/b770566f-6b6d-4fa3-a1ea-b74dc86c6090)



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
    ```

2. **Configure the database:**

    - Ensure MariaDB is installed and running.
    - Create a database named `library_db`.
   
    Or you can use the docker-compose file to run the MariaDB container. Make sure to be in the docker folder of the project.
    ```sh
      cd docker
      docker-compose -f docker-compose-db.yml up
    ```
   
3. **Set the environment variables:**

    - Create a `.env` file in the root directory of the project.
    ```sh
    public static final String DB_URL = "jdbc:mariadb://localhost:3306/library_db";
    public static final String USER = "library_user";
    public static final String PASSWORD = "library_password";
    ```

4. **Install sample data:**

    - Run the `SampleDataInserter` file to insert sample data into the database.
   

5. **Build the project:**
    ```sh
    mvn clean install
    ```

6. **Run the application:**
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
- docker/docker-compose.yml: Contains the Docker Compose configuration (mariadb and luku).
- JenkinsFile: Contains the Jenkins pipeline configuration.
- SampleDataInserter.java: Inserts sample data into the database.
- pom.xml: Contains the Maven project configuration.
- Dockerfile: Contains the Docker image configuration.

## Jenkins Setup
**Install Jenkins:**
Download and install Jenkins from the official website.

**Configure Jenkins:**

- Install necessary plugins (e.g., Docker, Maven).
- Set up credentials for Docker Hub.
- Create Jenkins Pipeline:
- Use the provided Jenkinsfile to create a pipeline job in Jenkins.

**Push to Docker Hub**

Push to docker hub through Jenkins pipeline.
Docker Hub Repository:
[Docker Hub](https://hub.docker.com/repository/docker/mahnoor95/luku)

**Docker Play with Jenkins**

**Pull Docker Image:**

```sh
  docker pull mahnoor95/luku:v1
```

## Running the Application with Docker

**Set the environment variables:**

- Create a .env.docker file in the root directory of the project.

Add the following code to the .env.docker file:
```sh
    public static final String DB_URL = "jdbc:mariadb://mariadb:3306/library_db";
    public static final String USER = "library_user";
    public static final String PASSWORD = "library_password";
```
The environment automatically sets the database URL to the mariadb container.

If you are running the application locally, the application automatically loads the default .env file and connects to the localhost.

**Build Docker Image:**
   
   ```sh
      docker build -t mahnoor95/luku:v1 .
   ```
**check docker image:**
   
   ```sh
      docker images
   ```

**Push the Docker image to Docker Hub:**

If you want to push your image to docker hub locally, you can use the following command:

   ```sh
      docker push mahnoor95/luku:v1
   ```
   
**Pull the Docker image from Docker Hub:**
   ```sh
     docker pull mahnoor95/luku:v1
   ```

## Running the Application with Docker Compose for macOS


**Set up XQuartz:**

```sh
   brew install --cask xquartz 
```

```sh
   open -a XQuartz
```

It will open X11. Go to the Security tab and check "Allow connections from network clients".

**Set the DISPLAY environment variable:**

```sh
    export DISPLAY=:0
```

**Allow connections to the X server:**

```sh
   xhost +localhost
```
**Run Docker Compose:**

Make sure you are in docker folder of the project. To run the Docker multiple container, use the following command:
```sh
  docker-compose up
```

## Running the Application with Docker Compose for Windows

**Install Chocolatey:**

```sh
  Set-ExecutionPolicy Bypass -Scope Process -Force; [System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072; iex ((New-Object System.Net.WebClient).DownloadString('https://community.chocolatey.org/install.ps1'))
  ```

```sh
  choco install xming
  ```

**X Server Setup**

After installing Xming, you need to configure XLaunch to run the X server.
Start by searching for XLaunch in the Windows search bar and running it. 
Alternatively, you can navigate to the following path: C:\Program Files (x86)\Xming and run the XLaunch executable.

If you cannot find the XLaunch executable, make sure that you have installed Xming correctly.

**Set the DISPLAY environment variable:** 

```sh
  set DISPLAY=localhost:0.0
```

**Allow connections to the X server:**

```sh
   xhost +localhost
```

**Run Docker Compose:**

Make sure you are in docker folder of the project. To run the Docker multiple container, use the following command:
    ```sh
    docker-compose up
    ```

## Data Persistence

The project uses MariaDB to store data. The changes made while running docker or locally will be persisted in the database. 
It is because the database is mounted to the host machine.


