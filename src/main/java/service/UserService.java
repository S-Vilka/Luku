
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
public class UserService {

    private final UserDao userDao;

    public UserService() {
        this.userDao = new UserDao();
    }

    public boolean authenticateUser(String email, String password) {
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

    public void registerUser(User user) {
        // Hash the password before saving
        try {
            var password = this.hashPassword(user.getPassword());
            user.setPassword(password); // Save the hashed password
            userDao.saveUser(user);
            String token = JwtUtil.generateToken(user.getUsername());
            AuthManager.getInstance().setToken(token);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public User getUserByEmail(String email) {
        return userDao.getUserByEmail(email);
    }

    public String hashPassword(String password) throws Exception {
        String salt = "randomSalt123"; // Store this key somewhere safe

        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536, 128);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        byte[] hash = factory.generateSecret(spec).getEncoded();
        return Base64.getEncoder().encodeToString(hash);
    }


    public int getUserBookCount(Long userId) {
        return userDao.getUserBookCount(userId);
    }

    public void decreaseUserBookCount(Long userId) {
        userDao.decreaseUserBookCount(userId);
    }


    public void updateUser(User user) {

        userDao.updateUser(user);
    }

    public User getUserById(Long Id) {

        return userDao.getUserById(Id);
    }

    public String getUserRole(Long userId) {
        return userDao.getUserRole(userId);
    }

    public String getUserPhone(String email) {
        return userDao.getUserPhone(email);
    }
}


