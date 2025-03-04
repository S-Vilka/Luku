package model.dao.impl;

import model.entity.User;
import java.sql.*;

public class UserDao extends BaseDao {

    public void saveUser(User user) {
        String query = "INSERT INTO users (username, password, email, phone, role, book_count, created_at, deleted_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getPhone());
            stmt.setString(5, user.getRole());
            stmt.setInt(6, user.getBookCount());
            stmt.setTimestamp(7, user.getCreatedAt() != null ? Timestamp.valueOf(user.getCreatedAt()) : null);
            stmt.setTimestamp(8, user.getDeletedAt() != null ? Timestamp.valueOf(user.getDeletedAt()) : null);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public User getUserById(Long userId) {
        String query = "SELECT * FROM users WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
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

    public User getUserByEmail(String email) {
        String query = "SELECT * FROM users WHERE email = ? limit 1";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
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

    public User updateUser(User user) {
        String query = "UPDATE users SET username = ?, password = ?, email = ?, phone = ?, role = ?, book_count = ?, created_at = ?, deleted_at = ? WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getPhone());
            stmt.setString(5, user.getRole());
            stmt.setInt(6, user.getBookCount());
            stmt.setTimestamp(7, user.getCreatedAt() != null ? Timestamp.valueOf(user.getCreatedAt()) : null);
            stmt.setTimestamp(8, user.getDeletedAt() != null ? Timestamp.valueOf(user.getDeletedAt()) : null);
            stmt.setLong(9, user.getUserId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }


    private User mapRowToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserId(rs.getLong("user_id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setEmail(rs.getString("email"));
        user.setPhone(rs.getString("phone"));
        user.setRole(rs.getString("role"));
        user.setBookCount(rs.getInt("book_count"));
        Timestamp createdAt = rs.getTimestamp("created_at");
        user.setCreatedAt(createdAt != null ? createdAt.toLocalDateTime() : null);
        Timestamp deletedAt = rs.getTimestamp("deleted_at");
        user.setDeletedAt(deletedAt != null ? deletedAt.toLocalDateTime() : null);
        return user;
    }

    public int getUserBookCount(Long userId) {
        String query = "SELECT book_count FROM users WHERE user_id = ?";
        int bookCount = 0;
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
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

    public void decreaseUserBookCount(Long userId) {
        String query = "UPDATE users SET book_count = book_count - 1 WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getUserRole(Long userId) {
        String query = "SELECT role FROM users WHERE user_id = ?";
        String role = null;
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
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

    public String getUserPhone(String email){
        String query = "SELECT phone FROM users WHERE email = ?";
        String phone = null;
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
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

    public void setNewPassword(String email, String newPassword) {
        String query = "UPDATE users SET password = ? WHERE email = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, newPassword);
            stmt.setString(2, email);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}