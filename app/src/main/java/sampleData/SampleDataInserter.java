//package sampleData;
//
//import config.DatabaseConfig;
//import model.dao.impl.BaseDao;
//import model.dao.impl.UserDao;
//import model.entity.User;
//import java.sql.Connection;
//import java.sql.Statement;
//import java.time.LocalDateTime;
//
//public class SampleDataInserter {
//    public static void main(String[] args) {
//        try {
//            Connection connection = DatabaseConfig.getConnection();
//            BaseDao.setConnection(connection);
//            UserDao userDao = new UserDao();
//
//            // Drop the notifications table if it exists
//            try (Statement stmt = connection.createStatement()) {
//                stmt.execute("DROP TABLE IF EXISTS notifications");
//            }
//
//            // Drop the reservations table if it exists
//            try (Statement stmt = connection.createStatement()) {
//                stmt.execute("DROP TABLE IF EXISTS reservations");
//            }
//
//            // Drop the books table if it exists
//            try (Statement stmt = connection.createStatement()) {
//                stmt.execute("DROP TABLE IF EXISTS books");
//            }
//
//            // Drop the authors table if it exists
//            try (Statement stmt = connection.createStatement()) {
//                stmt.execute("DROP TABLE IF EXISTS authors");
//            }
//
//            // Drop the users table if it exists
//            try (Statement stmt = connection.createStatement()) {
//                stmt.execute("DROP TABLE IF EXISTS users");
//            }
//
//            // Create the users table
//            try (Statement stmt = connection.createStatement()) {
//                stmt.execute("CREATE TABLE IF NOT EXISTS users (" +
//                        "user_id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
//                        "username VARCHAR(255) NOT NULL, " +
//                        "password VARCHAR(255) NOT NULL, " +
//                        "email VARCHAR(255) NOT NULL, " +
//                        "phone VARCHAR(20), " +
//                        "role VARCHAR(50), " +
//                        "book_count INT, " +
//                        "created_at TIMESTAMP, " +
//                        "deleted_at TIMESTAMP" +
//                        ")");
//            }
//
//            // Insert sample users
//            User user1 = new User();
//            user1.setUsername("user1");
//            user1.setPassword("password1");
//            user1.setEmail("user1@example.com");
//            user1.setPhone("1234567890");
//            user1.setRole("student");
//            user1.setBookCount(0);
//            user1.setCreatedAt(LocalDateTime.now());
//            user1.setDeletedAt(null);
//            userDao.saveUser(user1);
//
//            User user2 = new User();
//            user2.setUsername("user2");
//            user2.setPassword("password2");
//            user2.setEmail("user2@example.com");
//            user2.setPhone("0987654321");
//            user2.setRole("teacher");
//            user2.setBookCount(0);
//            user2.setCreatedAt(LocalDateTime.now());
//            user2.setDeletedAt(null);
//            userDao.saveUser(user2);
//
//            System.out.println("Sample users inserted successfully!");
//
//            // Create the books table
//            try (Statement stmt = connection.createStatement()) {
//                stmt.execute("CREATE TABLE IF NOT EXISTS books (" +
//                        "book_id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
//                        "title VARCHAR(255) NOT NULL, " +
//                        "publication_date DATE, " +
//                        "description TEXT, " +
//                        "availability_status VARCHAR(50), " +
//                        "category VARCHAR(100), " +
//                        "language VARCHAR(50), " +
//                        "isbn VARCHAR(20) NOT NULL, " +
//                        "location VARCHAR(255)" +
//                        ")");
//            }
//
//            // Insert sample books
//            try (Statement stmt = connection.createStatement()) {
//                stmt.execute("INSERT INTO books (title, publication_date, description, availability_status, category, language, isbn, location) VALUES " +
//                        "('Book One', '2021-01-01', 'Description of Book One', 'Available', 'Fiction', 'English', '111-1111111111', 'Shelf A1'), " +
//                        "('Book Two', '2022-02-02', 'Description of Book Two', 'Checked Out', 'Non-Fiction', 'English', '222-2222222', 'Shelf B2')");
//            }
//
//            System.out.println("Sample books inserted successfully!");
//
//            // Create the authors table
//            try (Statement stmt = connection.createStatement()) {
//                stmt.execute("CREATE TABLE IF NOT EXISTS authors (" +
//                        "author_id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
//                        "first_name VARCHAR(255) NOT NULL, " +
//                        "last_name VARCHAR(255) NOT NULL, " +
//                        "description TEXT, " +
//                        "date_of_birth DATE, " +
//                        "place_of_birth VARCHAR(255)" +
//                        ")");
//            }
//
//            // Create the reservations table
//            try (Statement stmt = connection.createStatement()) {
//                stmt.execute("CREATE TABLE IF NOT EXISTS reservations (" +
//                        "reservation_id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
//                        "user_id BIGINT NOT NULL, " +
//                        "book_id BIGINT NOT NULL, " +
//                        "borrow_date TIMESTAMP, " +
//                        "due_date TIMESTAMP, " +
//                        "FOREIGN KEY (user_id) REFERENCES users(user_id), " +
//                        "FOREIGN KEY (book_id) REFERENCES books(book_id)" +
//                        ")");
//            }
//
//            // Create the notifications table
//            try (Statement stmt = connection.createStatement()) {
//                stmt.execute("CREATE TABLE IF NOT EXISTS notifications (" +
//                        "notification_id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
//                        "user_id BIGINT NOT NULL, " +
//                        "message TEXT, " +
//                        "created_at TIMESTAMP, " +
//                        "reservation_id BIGINT NOT NULL, " +
//                        "FOREIGN KEY (user_id) REFERENCES users(user_id), " +
//                        "FOREIGN KEY (reservation_id) REFERENCES reservations(reservation_id)" +
//                        ")");
//            }
//
//            System.out.println("Tables created successfully!");
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}

package sampleData;

import config.DatabaseConfig;
import model.dao.impl.BaseDao;
import model.dao.impl.UserDao;
import model.entity.User;
import model.entity.Author;
import model.entity.Book;
import model.entity.Reservation;
import model.entity.Notification;
import model.entity.Writes;

import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class SampleDataInserter {
    public static void main(String[] args) {
        try {
            Connection connection = DatabaseConfig.getConnection();
            BaseDao.setConnection(connection);
            UserDao userDao = new UserDao();

            // Drop existing tables
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("DROP TABLE IF EXISTS notifications, reservations, writes, books, authors, users");
            }

            // Create the users table
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("CREATE TABLE IF NOT EXISTS users (" +
                        "user_id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                        "username VARCHAR(255) NOT NULL, " +
                        "password VARCHAR(255) NOT NULL, " +
                        "email VARCHAR(255) NOT NULL, " +
                        "phone VARCHAR(20), " +
                        "role VARCHAR(50), " +
                        "book_count INT, " +
                        "created_at TIMESTAMP, " +
                        "deleted_at TIMESTAMP" +
                        ")");
            }

            // Insert sample users
            User user1 = new User();
            user1.setUsername("user1");
            user1.setPassword("password1");
            user1.setEmail("user1@example.com");
            user1.setPhone("1234567890");
            user1.setRole("student");
            user1.setBookCount(0);
            user1.setCreatedAt(LocalDateTime.now());
            user1.setDeletedAt(null);
            userDao.saveUser(user1);

            User user2 = new User();
            user2.setUsername("user2");
            user2.setPassword("password2");
            user2.setEmail("user2@example.com");
            user2.setPhone("0987654321");
            user2.setRole("teacher");
            user2.setBookCount(0);
            user2.setCreatedAt(LocalDateTime.now());
            user2.setDeletedAt(null);
            userDao.saveUser(user2);

            System.out.println("Sample users inserted successfully!");

            // Create the authors table
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("CREATE TABLE IF NOT EXISTS authors (" +
                        "author_id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                        "first_name VARCHAR(255) NOT NULL, " +
                        "last_name VARCHAR(255) NOT NULL, " +
                        "description TEXT, " +
                        "date_of_birth DATE, " +
                        "place_of_birth VARCHAR(255)" +
                        ")");
            }

            // Insert sample authors
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("INSERT INTO authors (first_name, last_name, description, date_of_birth, place_of_birth) VALUES " +
                        "('John', 'Doe', 'Famous author', '1970-01-01', 'New York'), " +
                        "('Jane', 'Smith', 'Renowned writer', '1980-02-02', 'Los Angeles')");
            }

            System.out.println("Sample authors inserted successfully!");

            // Create the books table
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("CREATE TABLE IF NOT EXISTS books (" +
                        "book_id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                        "title VARCHAR(255) NOT NULL, " +
                        "publication_date DATE, " +
                        "description TEXT, " +
                        "availability_status VARCHAR(50), " +
                        "category VARCHAR(100), " +
                        "language VARCHAR(50), " +
                        "isbn VARCHAR(20) NOT NULL, " +
                        "location VARCHAR(255)" +
                        ")");
            }

            // Insert sample books
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("INSERT INTO books (title, publication_date, description, availability_status, category, language, isbn, location) VALUES " +
                        "('Book One', '2021-01-01', 'Description of Book One', 'Available', 'Fiction', 'English', '111-1111111111', 'Shelf A1'), " +
                        "('Book Two', '2022-02-02', 'Description of Book Two', 'Checked Out', 'Non-Fiction', 'English', '222-2222222', 'Shelf B2')");
            }

            System.out.println("Sample books inserted successfully!");

            // Create the writes table
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("CREATE TABLE IF NOT EXISTS writes (" +
                        "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                        "book_id BIGINT NOT NULL, " +
                        "author_id BIGINT NOT NULL, " +
                        "FOREIGN KEY (book_id) REFERENCES books(book_id), " +
                        "FOREIGN KEY (author_id) REFERENCES authors(author_id)" +
                        ")");
            }

            // Insert sample writes
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("INSERT INTO writes (book_id, author_id) VALUES " +
                        "(1, 1), " +
                        "(2, 2)");
            }

            System.out.println("Sample writes inserted successfully!");

            // Create the reservations table
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("CREATE TABLE IF NOT EXISTS reservations (" +
                        "reservation_id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                        "user_id BIGINT NOT NULL, " +
                        "book_id BIGINT NOT NULL, " +
                        "borrow_date TIMESTAMP, " +
                        "due_date TIMESTAMP, " +
                        "FOREIGN KEY (user_id) REFERENCES users(user_id), " +
                        "FOREIGN KEY (book_id) REFERENCES books(book_id)" +
                        ")");
            }

            // Insert sample reservations
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("INSERT INTO reservations (user_id, book_id, borrow_date, due_date) VALUES " +
                        "(1, 1, '2023-01-01 10:00:00', '2023-01-15 10:00:00'), " +
                        "(2, 2, '2023-02-01 10:00:00', '2023-02-15 10:00:00')");
            }

            System.out.println("Sample reservations inserted successfully!");

            // Create the notifications table
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("CREATE TABLE IF NOT EXISTS notifications (" +
                        "notification_id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                        "user_id BIGINT NOT NULL, " +
                        "message TEXT, " +
                        "created_at TIMESTAMP, " +
                        "reservation_id BIGINT NOT NULL, " +
                        "FOREIGN KEY (user_id) REFERENCES users(user_id), " +
                        "FOREIGN KEY (reservation_id) REFERENCES reservations(reservation_id)" +
                        ")");
            }

            // Insert sample notifications
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("INSERT INTO notifications (user_id, message, created_at, reservation_id) VALUES " +
                        "(1, 'Your book is due soon', '2023-01-10 10:00:00', 1), " +
                        "(2, 'Your book is overdue', '2023-02-20 10:00:00', 2)");
            }

            System.out.println("Sample notifications inserted successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}