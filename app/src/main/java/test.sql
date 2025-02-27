-- Create the database
CREATE DATABASE library_db;

-- Create a user and grant privileges
CREATE USER 'library_user'@'localhost' IDENTIFIED BY 'library_password';

-- Grant all necessary privileges on the database to the user
GRANT ALL PRIVILEGES ON library_db.* TO 'library_user'@'localhost';

-- Apply changes
FLUSH PRIVILEGES;