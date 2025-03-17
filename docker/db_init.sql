USE library_db;
-- Drop existing tables if they exist
DROP TABLE IF EXISTS notifications, reservations, writes, books, authors, users;

-- Create the users table
CREATE TABLE IF NOT EXISTS users (
     user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
     username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    phone VARCHAR(20),
    role VARCHAR(50),
    book_count INT,
    created_at TIMESTAMP,
    deleted_at TIMESTAMP
);

-- Insert sample users
INSERT INTO users (username, password, email, phone, role, book_count, created_at, deleted_at) VALUES
                                                                                                   ('user1', 'password1', 'user1@example.com', '1234567890', 'student', 0, NOW(), NULL),
                                                                                                   ('user2', 'password2', 'user2@example.com', '0987654321', 'teacher', 0, NOW(), NULL);

-- Create the authors table
CREATE TABLE IF NOT EXISTS authors (
                                       author_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                       first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    description TEXT,
    date_of_birth DATE,
    place_of_birth VARCHAR(255),
    profile_image VARCHAR(255)
    );

-- Insert sample authors
INSERT INTO authors (first_name, last_name, description, date_of_birth, place_of_birth, profile_image) VALUES
                                                                                                           ('John', 'Doe', 'Famous author', '1970-01-01', 'New York', 'john_doe.png'),
                                                                                                           ('Jane', 'Smith', 'Renowned writer', '1980-02-02', 'Los Angeles', 'jane_smith.png'),
                                                                                                           ('Alice', 'Johnson', 'Science fiction author', '1965-03-03', 'Chicago', 'alice_johnson.png'),
                                                                                                           ('Bob', 'Brown', 'History expert', '1955-04-04', 'Boston', 'bob_brown.png'),
                                                                                                           ('Bill', 'Black', 'History research', '1995-12-01', 'Sacramento', NULL);

-- Create the books table
CREATE TABLE IF NOT EXISTS books (
                                     book_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     title VARCHAR(255) NOT NULL,
    publication_date DATE,
    description TEXT,
    availability_status VARCHAR(50),
    category VARCHAR(100),
    language VARCHAR(50),
    isbn VARCHAR(20) NOT NULL,
    location VARCHAR(255),
    cover_image VARCHAR(255)
    );

-- Insert sample books
INSERT INTO books (title, publication_date, description, availability_status, category, language, isbn, location, cover_image) VALUES
                                                                                                                                   ('Book One', '2021-01-01', 'Description of Book One', 'Available', 'Fiction', 'English', '111-1111111111', 'Shelf A1', 'bookpicture.jpg'),
                                                                                                                                   ('Book Two', '2022-02-02', 'Description of Book Two', 'Checked Out', 'Non-Fiction', 'English', '222-2222222', 'Shelf B2', 'book_two.jpg'),
                                                                                                                                   ('Science Book', '2020-03-03', 'Description of Science Book', 'Available', 'Fiction', 'Finnish', '333-3333333', 'Shelf C3', 'science.png'),
                                                                                                                                   ('History Book', '2019-04-04', 'Description of History Book', 'Available', 'Fiction', 'Swedish', '444-4444444', 'Shelf D4', 'history.jpg'),
                                                                                                                                   ('Fictional Tales', '2018-05-05', 'Description of Fictional Tales', 'Available', 'Fiction', 'English', '555-5555555', 'Shelf E5', 'fictional.jpg'),
                                                                                                                                   ('Non-Fictional Stories', '2017-06-06', 'Description of Non-Fictional Stories', 'Available', 'Non-Fiction', 'Swedish', '666-6666666', 'Shelf F6', 'non_fictional.webp'),
                                                                                                                                   ('Advanced Science', '2016-07-07', 'Description of Advanced Science', 'Available', 'Science', 'Finnish', '777-7777777', 'Shelf G7', 'advanced_science.jpg'),
                                                                                                                                   ('World History', '2015-08-08', 'Description of World History', 'Available', 'History', 'English', '888-8888888', 'Shelf H8', 'world_history.png');

-- Create the writes table
CREATE TABLE IF NOT EXISTS writes (
                                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                      book_id BIGINT NOT NULL,
                                      author_id BIGINT NOT NULL,
                                      FOREIGN KEY (book_id) REFERENCES books(book_id),
    FOREIGN KEY (author_id) REFERENCES authors(author_id)
    );

-- Insert sample writes
INSERT INTO writes (book_id, author_id) VALUES
                                            (1, 1), (2, 2), (3, 3), (4, 4), (5, 1), (6, 2), (7, 3), (8, 4);

-- Create the reservations table
CREATE TABLE IF NOT EXISTS reservations (
                                            reservation_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                            user_id BIGINT NOT NULL,
                                            book_id BIGINT NOT NULL,
                                            borrow_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                            due_date TIMESTAMP NOT NULL,
                                            FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (book_id) REFERENCES books(book_id)
    );

-- Insert sample reservations
INSERT INTO reservations (user_id, book_id, borrow_date, due_date) VALUES
    (1, 1, '2024-03-09 10:00:00', '2024-03-23 10:00:00');

-- Create the notifications table
CREATE TABLE IF NOT EXISTS notifications (
                                             notification_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                             user_id BIGINT NOT NULL,
                                             message TEXT,
                                             created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                             reservation_id BIGINT NOT NULL,
                                             FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (reservation_id) REFERENCES reservations(reservation_id)
    );