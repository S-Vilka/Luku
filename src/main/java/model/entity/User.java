package model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import java.time.LocalDateTime;


/** * User entity class representing a user in the system.
 * This class is mapped to the "users" table in the database.
 */
@Entity
@Table(name = "users")
public class User {
    /**
     * Unique identifier for the user.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    /**
     * The username of the user.
     */
    private String username;
    /**
     * The password of the user.
     */
    private String password;
    /**
     * The email of the user.
     */
    private String email;
    /**
     * The phone number of the user.
     */
    private String phone;
    /**
     * The role of the user (e.g., admin, user).
     */
    private String role;
    /**
     * The number of books associated with the user.
     */
    private int bookCount;
    /**
     * The date and time when the user was created.
     */
    private LocalDateTime createdAt;
    /**
     * The date and time when the user was deleted (if applicable).
     */
    private LocalDateTime deletedAt;

    /**
     * Default constructor for the User class.
     */
    public User() {
    }

    /**
     * Constructor for the User class with userId.
     * @param userIdSelected The unique identifier for the user.
     */
    public User(final Long userIdSelected) {
        this.userId = userIdSelected;
    }

    /**
     * @return userId
     */
    public Long getUserId() {
        return userId;
    }
    /**
     * Sets the userId.
     * @param userIdSelected The unique identifier for the user.
     */
    public void setUserId(final Long userIdSelected) {
        this.userId = userIdSelected;
    }

    /**
     * @return username
     */
    public String getUsername() {
        return username;
    }
    /**
     * Sets the username.
     * @param usernameSelected The username of the user.
     */

    public void setUsername(final String usernameSelected) {
        this.username = usernameSelected;
    }
    /**
     * @return password
     */

    public String getPassword() {
        return password;
    }

    /**
     * Sets the password.
     * @param passwordSelected The password of the user.
     */
    public void setPassword(final String passwordSelected) {
        this.password = passwordSelected;
    }

    /**
     * @return email
     */
    public String getEmail() {
        return email;
    }
    /**
     * Sets the email.
     * @param emailSelected The email of the user.
     */

    public void setEmail(final String emailSelected) {
        this.email = emailSelected;
    }

    /**
     * @return phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Sets the phone number.
     * @param phoneSelected The phone number of the user.
     */

    public void setPhone(final String phoneSelected) {
        this.phone = phoneSelected;
    }

    /**
     * @return role
     */
    public String getRole() {
        return role;
    }

    /**
     * Sets the role.
     * @param roleSelected The role of the user (e.g., admin, user).
     */
    public void setRole(final String roleSelected) {
        this.role = roleSelected;
    }

    /**
     * @return bookCount
     */

    public int getBookCount() {
        return bookCount;
    }

    /**
     * Sets the book count.
     * @param bookCountSelected The number of books associated with the user.
     */
    public void setBookCount(final int bookCountSelected) {
        this.bookCount = bookCountSelected;
    }

    /**
     * @return createdAt
     */

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the createdAt.
     * @param createdAtSelected The date and time when the user was created.
     */

    public void setCreatedAt(final LocalDateTime createdAtSelected) {
        this.createdAt = createdAtSelected;
    }

    /**
     * @return deletedAt
     */
    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    /**
     * Sets the deletedAt.
     * @param deletedAtSelected The date and time
     * when the user was deleted (if applicable).
     */
    public void setDeletedAt(final LocalDateTime deletedAtSelected) {
        this.deletedAt = deletedAtSelected;
    }
}
