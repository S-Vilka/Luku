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
- Notoify users about due dates
- User can reserve limited books based on their role.

## Technologies Used
- Java
- Maven
- MariaDB
- JUnit 5
- Mockito
- Docker
- Testcontainers

## Prerequisites
- Java 11 or higher
- Maven 3.6 or higher
- MariaDB
- Docker

## Setup and Installation

1. **Clone the repository:**
    ```sh
    git clone https://github.com/MahnoorFatima02/library-management-system.git
    cd library-management-system
    ```

2. **Configure the database:**
    - Ensure MariaDB is installed and running.
    - Create a database named `library_db`.
    - Update the database connection details in the `application.properties` file.

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

## Running Tests
The project includes unit tests for the service and controller layers. To run the tests, use the following command:
```sh
mvn test
```

## Project Structure
- src/main/java: Contains the main application code.
- src/test/java: Contains the test code.
- src/main/resources: Contains configuration files.