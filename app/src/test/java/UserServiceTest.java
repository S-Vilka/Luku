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
            user.setBookCount(0);
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
            System.out.println("fetchedUser: " + fetchedUser);
                if (fetchedUser != null) {
                    System.out.println("User fetched by email: " + fetchedUser.getUsername());
                    var token = JwtUtil.generateToken(fetchedUser.getUsername());
                    System.out.println("Token: " + token);
                    JwtUtil.parseToken(token);
                } else {
                    System.out.println("In test email.");
                    System.out.println("User not found.");
                }
            }
        }
