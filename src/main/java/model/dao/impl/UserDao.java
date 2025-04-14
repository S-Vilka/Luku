package model.dao.impl;

import model.entity.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class UserDao {
    /**
     * The username of the user.
     */
    private static final int USERNAME_INDEX = 1;
    /**
     * The password of the user.
     */
    private static final int PASSWORD_INDEX = 2;
    /**
     * The email of the user.
     */
    private static final int EMAIL_INDEX = 3;
    /**
     * The phone number of the user.
     */
    private static final int PHONE_INDEX = 4;
    /**
     * The role of the user (e.g., admin, user).
     */
    private static final int ROLE_INDEX = 5;
    /**
     * The number of books associated with the user.
     */
    private static final int BOOK_COUNT_INDEX = 6;
    /**
     * The date and time when the user was created.
     */
    private static final int CREATED_AT_INDEX = 7;
    /**
     * The date and time when the user was deleted (if applicable).
     */
    private static final int DELETED_AT_INDEX = 8;
    /**
     * The unique identifier for the user.
     */
    private static final int USER_ID_INDEX = 9;

    /**
     * Saves a new user to the database.
     * @param user The user object to be saved.
     */
    public void saveUser(final User user) {
        String query = "INSERT INTO users (username, password, email, "
                + "phone, role, book_count, created_at, deleted_at) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = BaseDao
                .getConnection().prepareStatement(query)) {
            stmt.setString(USERNAME_INDEX, user.getUsername());
            stmt.setString(PASSWORD_INDEX, user.getPassword());
            stmt.setString(EMAIL_INDEX, user.getEmail());
            stmt.setString(PHONE_INDEX, user.getPhone());
            stmt.setString(ROLE_INDEX, user.getRole());
            stmt.setInt(BOOK_COUNT_INDEX, user.getBookCount());
            stmt.setTimestamp(CREATED_AT_INDEX, user.getCreatedAt() != null
                    ? Timestamp.valueOf(user.getCreatedAt()) : null);
            stmt.setTimestamp(DELETED_AT_INDEX, user.getDeletedAt() != null
                    ? Timestamp.valueOf(user.getDeletedAt()) : null);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * Retrieves a user by their ID.
     *
     * @param userId The ID of the user to be retrieved.
     * @return The user object if found, null otherwise.
     */
    public User getUserById(final Long userId) {
        String query = "SELECT * FROM users WHERE user_id = ?";
        try (PreparedStatement stmt = BaseDao.getConnection()
                .prepareStatement(query)) {
            stmt.setLong(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapRowToUser(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * Retrieves a user by their email.
     *
     * @param email The email of the user to be retrieved.
     * @return The user object if found, null otherwise.
     */
    public User getUserByEmail(final String email) {
        String query = "SELECT * FROM users WHERE email = ? limit 1";

        try (PreparedStatement stmt = BaseDao.getConnection()
                .prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapRowToUser(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * Updated a user in the database.
     *
     * @param user The username of the user to be retrieved.
     * @return The user object if found, null otherwise.
     */

    public User updateUser(final User user) {
        String query = "UPDATE users SET username = ?, password = ?, "
                + "email = ?, phone = ?, role = ?, "
                + "book_count = ?, created_at = ?,"
                + " deleted_at = ? WHERE user_id = ?";
        try (PreparedStatement stmt = BaseDao
                .getConnection().prepareStatement(query)) {
            stmt.setString(USERNAME_INDEX, user.getUsername());
            stmt.setString(PASSWORD_INDEX, user.getPassword());
            stmt.setString(EMAIL_INDEX, user.getEmail());
            stmt.setString(PHONE_INDEX, user.getPhone());
            stmt.setString(ROLE_INDEX, user.getRole());
            stmt.setInt(BOOK_COUNT_INDEX, user.getBookCount());
            stmt.setTimestamp(CREATED_AT_INDEX, user.getCreatedAt() != null
                    ? Timestamp.valueOf(user.getCreatedAt()) : null);
            stmt.setTimestamp(DELETED_AT_INDEX, user.getDeletedAt() != null
                    ? Timestamp.valueOf(user.getDeletedAt()) : null);
            stmt.setLong(USER_ID_INDEX, user.getUserId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    /**
     * maps the result set to a user object.
     * @param rs The ID of the user to be deleted.
     * @return The user object if found, null otherwise.
     */

    private User mapRowToUser(final ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserId(rs.getLong("user_id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setEmail(rs.getString("email"));
        user.setPhone(rs.getString("phone"));
        user.setRole(rs.getString("role"));
        user.setBookCount(rs.getInt("book_count"));
        Timestamp createdAt = rs.getTimestamp("created_at");
        user.setCreatedAt(createdAt != null
                ? createdAt.toLocalDateTime() : null);
        Timestamp deletedAt = rs.getTimestamp("deleted_at");
        user.setDeletedAt(deletedAt != null
                ? deletedAt.toLocalDateTime() : null);
        return user;
    }
    /**
     * Get the count of books associated with a user.
     * @param userId The ID of the user to be deleted.
        * @return The number of books associated with the user.
     */
    public int getUserBookCount(final Long userId) {
        String query = "SELECT book_count FROM users WHERE user_id = ?";
        int bookCount = 0;
        try (PreparedStatement stmt = BaseDao
                .getConnection().prepareStatement(query)) {
            stmt.setLong(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                bookCount = rs.getInt(1);
                System.out.println("User has " + bookCount + " books");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookCount;
    }
    /**
     * Decreases the book count of a user by 1.
     * @param userId The ID of the user to be deleted.
     */

    public void decreaseUserBookCount(final Long userId) {
        String query = "UPDATE users SET book_count = book_count - 1 "
                + "WHERE user_id = ?";
        try (PreparedStatement stmt = BaseDao
                .getConnection().prepareStatement(query)) {
            stmt.setLong(1, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     * Get the role of a user.
     * @param userId The ID of the user to be deleted.
     * @return The role of the user.
     */

    public String getUserRole(final Long userId) {
        String query = "SELECT role FROM users WHERE user_id = ?";
        String role = null;
        try (PreparedStatement stmt = BaseDao.getConnection()
                .prepareStatement(query)) {
            stmt.setLong(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                role = rs.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return role;
    }

    /**
     * Get the phone number of a user.
     * @param email The ID of the user to be deleted.
     * @return The phone number of the user.
     */
    public String getUserPhone(final String email) {
        String query = "SELECT phone FROM users WHERE email = ?";
        String phone = null;
        try (PreparedStatement stmt = BaseDao
                .getConnection().prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                phone = rs.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return phone;
    }
    /**
     * Set a new password for a user.
     * @param email The email of the user to be deleted.
        * @param newPassword The new password to be set.
     */

    public void setNewPassword(final String email, final String newPassword) {
        String query = "UPDATE users SET password = ? WHERE email = ?";
        try (PreparedStatement stmt = BaseDao
                .getConnection().prepareStatement(query)) {
            stmt.setString(1, newPassword);
            stmt.setString(2, email);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
