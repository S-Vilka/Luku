package service;

import model.entity.User;
import java.security.spec.KeySpec;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import model.dao.impl.UserDao;
import util.AuthManager;
import util.JwtUtil;

//@Service
public final class UserService {

    /**
     * Data access object for user-related database operations.
     */
    private final UserDao userDao;

    /**
     * The number of iterations used in the password hashing algorithm.
     */
    private static final int HASH_ITERATIONS = 65536;

    /**
     * The length (in bits) of the generated hash key.
     */
    private static final int KEY_LENGTH = 128;

    /**
     * Constructs a new UserService with a default UserDao instance.
     */
    public UserService() {
        this.userDao = new UserDao();
    }

    /**
     * Authenticates a user based on the provided email and password.
     * Generates and stores a JWT token upon successful authentication.
     *
     * @param email the user's email
     * @param password the user's plain-text password
     * @return true if authentication is successful, false otherwise
     */
    public boolean authenticateUser(final String email, final String password) {
        User user = userDao.getUserByEmail(email);
        if (user == null) {
            return false;
        }
        try {
            var hashedPassword = this.hashPassword(password);
            if (hashedPassword.equals(user.getPassword())) {
                String token = JwtUtil.generateToken(user.getUsername());
                AuthManager.getInstance().setToken(token);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
            return false;
    }

    /**
     * Registers a new user by hashing the password,
     * saving the user to the database, and generating a JWT token.
     *
     * @param user the user to be registered
     */
    public void registerUser(final User user) {
        // Hash the password before saving
        try {
            var password = this.hashPassword(user.getPassword());
            user.setPassword(password); // Save the hashed password
            userDao.saveUser(user);
            String token = JwtUtil.generateToken(user.getUsername());
            AuthManager.getInstance().setToken(token);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves a user by their email address.
     *
     * @param email the email of the user
     * @return the User object if found, otherwise null
     */
    public User getUserByEmail(final String email) {
        return userDao.getUserByEmail(email);
    }

    /**
     * Hashes the given password using PBKDF2 with HmacSHA256 and a static salt.
     *
     * @param password the plain-text password to hash
     * @return the hashed password as a Base64-encoded string
     * @throws Exception if the hashing algorithm fails
     */
    public String hashPassword(final String password) throws Exception {
        String salt = "randomSalt123"; // Store this key somewhere safe

        KeySpec spec = new PBEKeySpec(
                password.toCharArray(),
                salt.getBytes(),
                HASH_ITERATIONS,
                KEY_LENGTH
        );
        SecretKeyFactory factory =
                SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        byte[] hash = factory.generateSecret(spec).getEncoded();
        return Base64.getEncoder().encodeToString(hash);
    }


    /**
     * Retrieves the number of books currently associated with the given user.
     *
     * @param userId the ID of the user
     * @return the number of books the user has
     */
    public int getUserBookCount(final Long userId) {
        return userDao.getUserBookCount(userId);
    }

    /**
     * Decreases the number of books associated with the given user by one.
     *
     * @param userId the ID of the user
     */
    public void decreaseUserBookCount(final Long userId) {
        userDao.decreaseUserBookCount(userId);
    }


    /**
     * Updates the user's information in the database.
     *
     * @param user the user object containing updated information
     */
    public void updateUser(final User user) {

        userDao.updateUser(user);
    }

    /**
     * Retrieves a user by their unique ID.
     *
     * @param id the ID of the user
     * @return the User object if found, otherwise null
     */
    public User getUserById(final Long id) {
        return userDao.getUserById(id);
    }

    /**
     * Retrieves the role of a user by their ID.
     *
     * @param userId the ID of the user
     * @return the role of the user as a string
     */
    public String getUserRole(final Long userId) {
        return userDao.getUserRole(userId);
    }

    /**
     * Retrieves the phone number of a user by their email.
     *
     * @param email the email of the user
     * @return the user's phone number as a string, or null if not found
     */
    public String getUserPhone(final String email) {
        return userDao.getUserPhone(email);
    }
}


