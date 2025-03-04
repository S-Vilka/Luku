import config.DatabaseConfig;
import model.dao.impl.BaseDao;
import model.dao.impl.UserDao;
import model.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import service.UserService;
import util.JwtUtil;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
public class UserServiceTest {
    @Container
    private static final MariaDBContainer<?> mariaDBContainer = new MariaDBContainer<>("mariadb:10.5.9")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");

    private UserService userService;
    private UserDao userDao;

    @BeforeEach
    public void setUp() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(mariaDBContainer.getJdbcUrl(), mariaDBContainer.getUsername(), mariaDBContainer.getPassword());
            BaseDao.setConnection(connection);
            userDao = new UserDao();
            userService = new UserService();

            // Drop the users table if it exists
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("DROP TABLE IF EXISTS users");
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

            User user = new User();
            user.setUsername("newuser");
            user.setPassword("newpassword123");
            user.setEmail("newuser@example.com");
            user.setPhone("0987654321");
            user.setRole("student");
            user.setBookCount(3);
            user.setCreatedAt(LocalDateTime.now());
            user.setDeletedAt(null);
            userService.registerUser(user);

            User savedUser = userService.getUserByEmail("newuser@example.com");
            System.out.println("Saved user: " + savedUser);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testRegisterUser() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password123");
        user.setEmail("testuser@example.com");
        user.setPhone("1234567890");
        user.setRole("student");
        user.setBookCount(0);
        user.setCreatedAt(LocalDateTime.now());
        user.setDeletedAt(null);
        userService.registerUser(user);
        System.out.println("In testRegisterUser");
        System.out.println("User registered successfully!");

        User fetchedUser = userService.getUserByEmail("testuser@example.com");
        assertEquals("testuser", fetchedUser.getUsername());
    }



        @Test
        public void testGetUserByEmail() {
            User fetchedUser = userService.getUserByEmail("newuser@example.com");
                if (fetchedUser != null) {
                    var token = JwtUtil.generateToken(fetchedUser.getUsername());
                    JwtUtil.parseToken(token);
                } else {
                    System.out.println("In test email.");
                    System.out.println("User not found.");
                }
            }

        @Test
        public void testGetUserById() {
            User fetchedUser = userService.getUserByEmail("newuser@example.com");
            if (fetchedUser != null) {
                User user = userService.getUserById(fetchedUser.getUserId());
                System.out.println("In testGetUserById");
                System.out.println("User found: " + user);
            } else {
                System.out.println("User not found.");
            }
        }


    @Test
    public void testUpdateUser() {
        // Retrieve the user from the database
        User user = userService.getUserByEmail("newuser@example.com");
        if (user != null) {
            // Update the necessary fields
            user.setUsername("testUser");
            user.setPassword("testPass");
            user.setPhone("123456789");
            user.setRole("user");
            user.setBookCount(5);
            user.setCreatedAt(LocalDateTime.now());
            user.setDeletedAt(null);

            // Call the updateUser method
            userService.updateUser(user);
            System.out.println("In testUpdateUser");
            System.out.println("User updated successfully!");
        } else {
            System.out.println("User not found.");
        }
    }

    @Test
    public void testGetUserBookCount() {
        Long userId = 1L;
        int bookCount = userService.getUserBookCount(userId);
        System.out.println("In testGetUserBookCount");
        assertEquals(3, bookCount);
    }

    @Test
    public void testGetUserPhone() {
        Long userId = 1L;
        User user = userService.getUserById(userId);
        if (user != null) {
            String userEmail = user.getEmail();
            String phoneNumber = userService.getUserPhone(userEmail);
            assertEquals("0987654321", phoneNumber);
        }
    }

    @Test
    public void testGetUserRole() {
        Long userId = 1L;
        String role = userService.getUserRole(userId);
        assertEquals("student", role);
    }

    @Test
    public void testDecreaseUserBookCount() {
        Long userId = 1L;
        userService.decreaseUserBookCount(userId);
        int bookCount = userService.getUserBookCount(userId);
        assertEquals(2, bookCount);
    }

        }
